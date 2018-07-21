package com.github.yilmazbahadir.kalah.domain.model.impl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.yilmazbahadir.kalah.domain.model.*;
import com.github.yilmazbahadir.kalah.exception.GameStatusException;
import com.github.yilmazbahadir.kalah.exception.InvalidPitIndexException;
import com.github.yilmazbahadir.kalah.exception.PlayerInvalidTurnException;
import com.github.yilmazbahadir.kalah.exception.WrongMoveException;
import lombok.Getter;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h1>Kalah Implementation of Game Interface</h1>
 * This class controls the whole Kalah game components and contains
 * domain(game) specific logic.
 *
 * @author Bahadir Yilmaz
 * @version 1.0
 * Date:   Jul 2018
 */
@ToString
public class KalahGame implements Game {

    @Getter
    private long id;
    @Getter
    private String name;
    @Getter
    private Board board;
    @Getter
    private List<Player> players;
    @Getter
    private GameStatus status;
    @Getter
    private GameConfig config;
    @Getter
    private GameHistory history; //TODO nice to have

    @JsonIgnore
    private transient GameEventListener eventListener;


    private AtomicInteger playerCounter = new AtomicInteger(0);


    public KalahGame(long id, String name, GameEventListener eventListener) {
        this(id, name, eventListener, GameConfig.builder().build());
    }

    public KalahGame(long id, String name, GameEventListener eventListener, GameConfig config) {
        this.id = id;
        this.name = name;
        this.status = new KalahGameStatus(GameStatusType.NOT_STARTED);
        this.eventListener = eventListener;
        this.board = new KalahBoard(config.getNumOfPlayers(), config.getNumOfPits(), config.getNumOfStonesInEachPit());
        this.config = config;
        this.players = new CopyOnWriteArrayList<>();
    }

    /**
     * Decides which player should start(strategy: random) and starts the game
     * @return next player
     */
    @Override
    public synchronized int start() {
        //TODO if spring cloud eureka is used how would you implement distributed locking mechanism - zookeeper?
        if (this.getStatus().getStatusType() == GameStatusType.NOT_STARTED) {
            this.getStatus().setStatusType(GameStatusType.STARTED);
            this.getStatus().setCurrentPlayer(-1);
            this.getStatus().setNextPlayer(new Random().nextInt(this.getConfig().getNumOfPlayers())); // randomly selects a player to start
        } // else if the status is started or playing then do nothing
        return this.getStatus().getNextPlayer();
    }

    /**
     *
     * @param playerId
     * @param pitInx
     * @return
     * @throws GameStatusException
     * @throws PlayerInvalidTurnException
     * @throws WrongMoveException
     * @throws InvalidPitIndexException
     */
    @Override
    public synchronized GameStatus play(final int playerId, final int pitInx) throws GameStatusException, PlayerInvalidTurnException, WrongMoveException, InvalidPitIndexException {

        this.checkPlayValidations(playerId, pitInx);

        this.getStatus().setCurrentPlayer(this.getStatus().getNextPlayer());
        this.getStatus().setStatusType(GameStatusType.PLAYER_PLAYING);

        // Direction.COUNTER_CLOCK_WISE default direction is implemented for now

        PlayContext ctx = new PlayContext();
        ctx.numOfPlayers = this.getConfig().getNumOfPlayers();
        ctx.nextPlayer = (playerId + 1) % ctx.numOfPlayers;
        ctx.playerId = playerId;
        ctx.currentSideId = playerId;
        ctx.nextSideId = playerId;
        ctx.currentPitInx = pitInx + 1;
        ctx.stonesInHand = this.getBoard().getSide(playerId).getPit(pitInx).pick();

        //start to sow some stones
        while (ctx.stonesInHand > 0) {
            ctx.sowAmount = 1;
            this.moveToNextPitOrOwnHouse(ctx);
            if(ctx.stonesInHand == 1) { // last stone in hand
                this.applyLastStoneCurrentSide(ctx);
            }
            ctx.pitToBeSowed.sow(ctx.sowAmount);
            ctx.stonesInHand--;
            ctx.currentSideId = ctx.nextSideId % ctx.numOfPlayers;
            ctx.pitToBeSowed = null;
        }

        // check if the game is finished, if any player's pits are empty then it is finished
        if (Arrays.stream(this.getBoard().getSides()).anyMatch(s -> s.getTotalNumOfStonesInPits().intValue() == 0)) {
            // then all stones in the own pits will be put in owning players' houses
            Arrays.stream(this.getBoard().getSides()).forEach(s -> s.getHouse().sow(Arrays.stream(s.getPits()).mapToInt(p -> p.pick()).sum()));
            this.getStatus().setStatusType(GameStatusType.FINISHED);
            this.getStatus().setNextPlayer(-1); //an invalid value
        } else {
            this.getStatus().setNextPlayer(ctx.nextPlayer);
            this.getStatus().setStatusType(GameStatusType.WAITING_FOR_NEXT_PLAYER);
        }
        GameEvent evt = new GameEvent();
        evt.setType(GameEventType.GAME_REFRESH_EVENT);
        evt.setGame(this);
        this.eventListener.handle(evt);

        return this.getStatus();
    }

    /**
     * Checks if the move to be made is valid.
     *
     * @param playerId Current player ID
     * @param pitInx Pit Index intended to be picked
     * @throws GameStatusException
     * @throws PlayerInvalidTurnException
     * @throws InvalidPitIndexException
     * @throws WrongMoveException
     */
    private void checkPlayValidations(int playerId, int pitInx) throws GameStatusException, PlayerInvalidTurnException, InvalidPitIndexException, WrongMoveException {
        if (this.getStatus().getStatusType() != GameStatusType.WAITING_FOR_NEXT_PLAYER) {
            throw new GameStatusException(this.getStatus().getStatusType().toString());
        }

        if (this.getStatus().getNextPlayer() != playerId) { // wrong player
            throw new PlayerInvalidTurnException(this.getStatus().getNextPlayer() + "");
        }

        if (pitInx >= this.getConfig().getNumOfPits()) {
            throw new InvalidPitIndexException(pitInx);
        }

        if(this.getBoard().getSide(playerId).getPit(pitInx).count() == 0) {
            throw new WrongMoveException("You can't pick any from an empty pit.");
        }
    }

    /**
     * Finds the next movable pit or own house and set the play context accordingly
     *
     * @param ctx Context of the Play(Move)
     */
    private void moveToNextPitOrOwnHouse(final PlayContext ctx) {
        if (ctx.currentPitInx == this.getConfig().getNumOfPits()) { // if end of the pits in current side is reached
            ctx.nextSideId = (ctx.currentSideId + 1) % ctx.numOfPlayers; // will move to next side
            ctx.currentPitInx = 0; // setting next pit index
            if(ctx.currentSideId % ctx.numOfPlayers == ctx.playerId) { // next pit his own house, so set the house to be sowed first
                ctx.pitToBeSowed = this.getBoard().getSide(ctx.currentSideId).getHouse();
            } else {
                ctx.currentSideId = ctx.nextSideId; // move to the next side immediately
            }
        }
        if(ctx.pitToBeSowed == null) {
            ctx.pitToBeSowed = this.getBoard().getSide(ctx.currentSideId).getPit(ctx.currentPitInx++);
        }
    }


    /**
     * Applies the last stone rules to the play context.
     * Rules are :
     * - If the last stone is in own house/kalah then win a score and a free turn
     * - It the last stone is in an own empty pit then pick the opposite pit stones and with one in hand
     * sow to the player's house all.
     * @param ctx Context of the Play(Move)
     */
    private void applyLastStoneCurrentSide(final PlayContext ctx) {
        if (ctx.stonesInHand == 1 && ctx.currentSideId == ctx.playerId) { // last stone is in current player's side
            if (ctx.pitToBeSowed.getType() == Pit.PitType.HOUSE) { // his own house
                ctx.nextPlayer = ctx.playerId; // for another turn for the same player
            } else if(ctx.pitToBeSowed.count() == 0){ // regular empty pit
                // capture stones in the opposite pit & with the stone in hand, sow in player's house
                int oppositePitInx = this.getConfig().getNumOfPits() - 1 - ctx.pitToBeSowed.getIndex();
                int oppositeSideId = ctx.numOfPlayers - 1 - ctx.currentSideId;
                int capturedStones = this.getBoard().getSide(oppositeSideId).getPit(oppositePitInx).pick();
                ctx.pitToBeSowed = this.getBoard().getSide(ctx.playerId).getHouse();
                ctx.sowAmount = capturedStones + 1; // plus 1 in hand
            }
        }
    }

    /**
     *
     * @param playerName
     * @return
     */
    @Override
    public Player join(String playerName) {
        Player player = new KalahPlayer(playerCounter.getAndIncrement(), playerName);
        this.players.add(player);

        this.getStatus().setStatusType(GameStatusType.WAITING_FOR_NEXT_PLAYER);

        return player;
    }

    /**
     *
     * @param playerId
     * @return
     */
    @Override
    public List<Player> leave(long playerId) {
        this.players.stream().filter(p -> p.getId() == playerId).forEach(p -> this.players.remove(p));

        return this.players;
    }

    private static class PlayContext{
        private int numOfPlayers;
        private int playerId;
        private int nextPlayer;
        private int currentPitInx;
        private int currentSideId;
        private int nextSideId;
        private Pit pitToBeSowed;
        private int stonesInHand;
        private int sowAmount;
    }
}
