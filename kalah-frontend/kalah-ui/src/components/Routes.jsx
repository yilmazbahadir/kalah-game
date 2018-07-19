import React from 'react'
import { Switch, Route } from 'react-router-dom'
import GameList from './game/GameList';
import Game from './game/Game';
import JoinGame from './game/JoinGame';


const Routes = () => (
  <main>
    <Switch>
      <Route exact path='/kalah' component={GameList}/>
      <Route path='/kalah/game/:gameId/player/:playerId' component={Game}/>
      <Route path='/kalah/game/:gameId/join' component={JoinGame}/>
    </Switch>
  </main>
)

export default Routes
