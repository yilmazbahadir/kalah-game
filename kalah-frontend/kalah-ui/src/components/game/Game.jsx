import React from 'react';
import PropTypes from 'prop-types';
import classNames from 'classnames';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import IconButton from '@material-ui/core/IconButton';
import Icon from '@material-ui/core/Icon';
import Button from '@material-ui/core/Button';
import { fetchGame, play } from '../../redux/actions/thunks';
import { connect } from 'react-redux';
import PlayArrow from '@material-ui/icons/PlayArrow';
import Tooltip from '@material-ui/core/Tooltip';
import CreateGameDialog from './CreateGameDialog';
import JoinGame from './JoinGame';
import Flex from './../common/glamorous/Flex';
import SockJsClient from 'react-stomp';

class Game extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        playerId: this.props.match.params.playerId,
        gameId: this.props.match.params.gameId,
        game: {data: [], error:null, isFetching: true}
      };
    
  }
    
  componentDidMount() {
    this.props.fetchGame(this.props.match.params.gameId);
  } 

  componentWillReceiveProps(nextProps) {
    this.setState({
        game: nextProps.game
    })
}
  render() {
    if (!this.state.game) return <span />;

    const { error, data, isFetching } = this.state.game;
    let content = null;

    if (isFetching) {
        content = <span>Loading!</span>;
    } else if (error) {
        content =  <span>{ error.message }</span>
    } else if(data && data.data == null) {
        content = (<div><span>No game found!</span><br/>
        {this.createNewGameButton(this.props)}</div>);   
    } else {
        content = this.createGameContent(data.data);
    }
    
    return content;
  }
  
  createGameContent(data) {
    return ( <div>
      {
        data.board.sides.sort((a, b) => a.sideId != this.state.playerId ? a.sideId : b.sideId).map((side) => (
          <div>
            <Side id={side.sideId} pits={side.pits} house={side.house} opposite={side.sideId != this.state.playerId} currentPlayer={data.status.nextPlayer}
              onClick={(pitInx) => this.props.play(this.state.gameId, this.state.playerId, pitInx)}/>
          <br/>
          </div>
        ))

      }     
        <GameStatus currentPlayer={data.status.currentPlayer} nextPlayer={data.status.nextPlayer} statusType={data.status.statusType} />
        <SockJsClient
                    url={`http://localhost:8080/kalah/websocket`}
                    topics={['/topic/kalah']}
                    onMessage={(gameEvent) => this.handleEvent(gameEvent)} />
      </div>
    );
  }

  createNewGameButton(props) {
    let open = false;
    return (
    <div><br />
        <CreateGameDialog open={open} onCreated={() => {props.fetchGame(this.props.match.params.gameId); this.forceUpdate();}}/>
        </div>);
  }

  handleEvent(gameEvent) {
    //alert('Event geldi. Event:' + JSON.stringify(gameEvent));
    let game = this.state.game;
    game.data.data = gameEvent.game;

    this.setState({
      game: game
    })
  } 
}


const Pit = (props) => (
  <div className={props.type == 'HOUSE' ? 'house' : 'pit'} onClick={() => (props.onClick(props.index))}>
    <span>#{props.index} </span>
    <div> {props.numOfStones} </div>
  </div>
)

const Side = (props) => (
  <div id={props.id} className={ 'side' + (props.opposite ? ' opposite' : '') + (props.currentPlayer == props.id ? ' playing' : '') }>
      {
        props.pits.map(
          (p) => (<Pit type={p.type} index={p.index} numOfStones={p.numOfStones} 
            onClick={props.opposite ? () => alert('Play on your side!') : props.onClick}/>)
        )
      }
      <Pit type={props.house.type} index={props.house.index} numOfStones={props.house.numOfStones} />
  </div>

)
const GameStatus = (props) => (
  <div>
    <span>Player {props.currentPlayer} has played. </span>
    <br/>
    <span>Next player is {props.nextPlayer} </span>
    <br/>
    <div> Game status: {props.statusType} </div>
  </div>
)

Game.propTypes = {
};

export default connect(
    state => ({game: state.entities && state.entities.game}),
    { fetchGame, play }
)(Game);
