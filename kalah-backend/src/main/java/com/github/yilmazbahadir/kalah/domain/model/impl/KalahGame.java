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

import static com.github.yilmazbahadir.kalah.domain.model.GameConfig.Direction;

/**
 * <h1><What does it do ?></h1>
 * <A simple explanation>
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
     */
    @Override
    public synchronized void start() {
        //TODO if spring cloud eureka is used how would you implement distributed locking mechanism - zookeeper?
        if (this.getStatus().getStatusType() == GameStatusType.NOT_STARTED) {
            this.getStatus().setStatusType(GameStatusType.STARTED);
            this.getStatus().setNextPlayer(new Random().nextInt(this.getConfig().getNumOfPlayers())); // randomly selects a player to start
        } // else if the status is started or playing then do nothing
    }

    @Override
    public synchronized GameStatus play(final int playerId, final int pitInx) throws GameStatusException, PlayerInvalidTurnException, WrongMoveException, InvalidPitIndexException {
        if (this.getStatus().getStatusType() != GameStatusType.WAITING_FOR_NEXT_PLAYER) {
            throw new GameStatusException(this.getStatus().getStatusType().toString());
        }

        if (this.getStatus().getNextPlayer() != playerId) { // wrong player
            throw new PlayerInvalidTurnException(this.getStatus().getNextPlayer() + ""); //TODO set playername ?
        }

        if (pitInx >= this.getConfig().getNumOfPits()) {
            throw new InvalidPitIndexException(pitInx);
        }

        if(this.getBoard().getSide(playerId).getPit(pitInx).count() == 0) {
            throw new WrongMoveException("You can't pick any from an empty pit.");
        }

        this.getStatus().setCurrentPlayer(this.getStatus().getNextPlayer());
        this.getStatus().setStatusType(GameStatusType.PLAYER_PLAYING);

        int nextPlayer = playerId;

        if (Direction.COUNTER_CLOCK_WISE == this.getConfig().getDirection()) { // default direction is implemented for now

            int numOfPlayers = this.getConfig().getNumOfPlayers();
            nextPlayer = (playerId + 1) % numOfPlayers;

            int stonesInHand = this.getBoard().getSide(playerId).getPit(pitInx).pick();

            int currentSideId = playerId, nextSideId = playerId;
            int currentPitInx = pitInx + 1;

            //start to sow some stones
            while (stonesInHand > 0) {
                Pit pitToBeSowed = null;

                if (currentPitInx == this.getConfig().getNumOfPits()) { // if end of the pits in current side is reached
                    nextSideId = (currentSideId + 1) % numOfPlayers; // will move to next side
                    currentPitInx = 0; // setting next pit index
                    if(currentSideId % numOfPlayers == playerId) {// next pit his own house, so set the house to be sowed first
                        pitToBeSowed = this.getBoard().getSide(currentSideId).getHouse();
                    } else {
                        currentSideId = nextSideId; // move to the next side immediately
                    }
                }
                if(pitToBeSowed == null) {
                    pitToBeSowed = this.getBoard().getSide(currentSideId).getPit(currentPitInx++);
                }
                int sowAmount = 1;
                if (stonesInHand == 1 && currentSideId == playerId) { // last stone is in current player's side
                    if (pitToBeSowed.getType() == Pit.PitType.HOUSE) { // his own house
                        nextPlayer = playerId; // for another turn for the same player //TODO fire event?
                    } else if(pitToBeSowed.count() == 0){ // regular empty pit
                        // capture stones in the opposite pit & with the stone in hand, sow in player's house
                        int oppositePitInx = this.getConfig().getNumOfPits() - 1 - pitToBeSowed.getIndex();
                        int oppositeSideId = numOfPlayers - 1 - currentSideId;
                        int capturedStones = this.getBoard().getSide(oppositeSideId).getPit(oppositePitInx).pick();
                        pitToBeSowed = this.getBoard().getSide(playerId).getHouse();
                        sowAmount = capturedStones + 1; // plus 1 in hand
                    }
                }

                pitToBeSowed.sow(sowAmount);

                stonesInHand--;
                currentSideId = nextSideId % numOfPlayers;
            }
        }
        // check if the game is finished, if any player's pits are empty then it is finished
        if (Arrays.stream(this.getBoard().getSides()).anyMatch(s -> s.getTotalNumOfStonesInPits().intValue() == 0)) {
            // then all stones in the own pits will be put in owning players' houses
            Arrays.stream(this.getBoard().getSides()).forEach(s -> s.getHouse().sow(Arrays.stream(s.getPits()).mapToInt(p -> p.pick()).sum()));
            this.getStatus().setStatusType(GameStatusType.FINISHED);
            this.getStatus().setNextPlayer(-1); //an invalid value
        } else {
            this.getStatus().setNextPlayer(nextPlayer);
            this.getStatus().setStatusType(GameStatusType.WAITING_FOR_NEXT_PLAYER);
        }
        GameEvent evt = new GameEvent();
        evt.setType(GameEventType.GAME_REFRESH_EVENT);
        evt.setGame(this);
        this.eventListener.handle(evt);

        return this.getStatus();
    }

    @Override
    public Player join(String playerName) {
        Player player = new KalahPlayer(playerCounter.getAndIncrement(), playerName);
        this.players.add(player);

        return player;
    }

    @Override
    public List<Player> leave(long playerId) {
        this.players.stream().filter(p -> p.getId() == playerId).forEach(p -> this.players.remove(p));

        return this.players;
    }

}
