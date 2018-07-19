import { loadEntity } from 'redux-entity';
import GameListService from '../../services/domain/game-list-service';


export function fetchGameList() {
    return loadEntity(
        'games',
        GameListService.getGames()
    );
}

export function createGame(gameConfig, afterSuccess) {
    return loadEntity(
        'game',
        GameListService.createGame(gameConfig),
        {processors : {'afterSuccess': afterSuccess } }
    );
}

export function joinGame(gameId, playerName, afterSuccess) {
    return loadEntity(
        'player',
        GameListService.joinGame(gameId, playerName),
        {processors : {'afterSuccess': afterSuccess } }
    );
}

export function fetchGame(gameId) {
    return loadEntity(
        'game',
        GameListService.fetchGame(gameId)
    );
}

export function play(gameId, playerId, pitInx, afterSuccess) {
    return loadEntity(
        'game',
        GameListService.play(gameId, playerId, pitInx),
        {processors : {'afterSuccess': afterSuccess } }
    );
}