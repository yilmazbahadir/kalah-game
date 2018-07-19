import DataAccessService from '../data/data-access-service';

function _getShortDelay () {
    return _getRandomDelayBetween(1, 3, 2);
}

function _getRandomDelayBetween (min, max, roundTo) {
    return Number(Math.random() * (max - min) + min).toFixed(roundTo);
}

const GameListService = {
    /**
     * List Games
     * @returns {*}
     */
    getGames () {
        return DataAccessService.get('/kalah/');
    },
    joinGamePromise (gameId, playerName) {
        return new Promise((resolve, reject) => {
            GameListService.joinGame(gameId, playerName);
        });
    },
    createGame(gameConfig) {
        return DataAccessService.post('/kalah/', gameConfig);
    },
    joinGame(gameId, playerName) {
        return DataAccessService.get('/kalah/' + gameId + '/' + playerName +'/join');
    },
    fetchGame(gameId) {
        return DataAccessService.get('/kalah/' + gameId);
    },
    play(gameId, playerId, pitInx) {
        return DataAccessService.get('/kalah/' + gameId + '/' + playerId + '/play/' + pitInx);
    }
};

export default GameListService;
