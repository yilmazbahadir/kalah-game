import React from 'react';
import { fetchGame, play } from '../../redux/actions/thunks';
import { connect } from 'react-redux';
import CreateGameDialog from './CreateGameDialog';
import SockJsClient from 'react-stomp';
import MessagePrompter from './MessagePrompter';
import ConfigService from './../../services/common/config-service'

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
    let content = null, messageContent='';

    if (isFetching) {
        return <span>Loading!</span>;
    } else if (error) {
        messageContent =  (<MessagePrompter id='gamePlayMessage' open={true} message={error.response.data.message} />);
    } else if(data && data.data == null) {
        content = (<div><span>No game found!</span><br/>
        {this.createNewGameButton(this.props)}</div>);   
        return content;
    } else {
      this.gameData = data.data;
    }

    content = this.createGameContent(this.gameData, messageContent);
    
    
    return content;
  }
  
  createGameContent(data, messageContent) {
    let viewer = this.state.playerId;
    return ( <div>
      { messageContent }
      { 
        data.board.sides.slice().sort((a, b) => a.sideId != viewer ? a.sideId : b.sideId).map((side) => (
          <div>
            <Side id={side.sideId} pits={side.pits} house={side.house} opposite={side.sideId != viewer} currentPlayer={data.status.nextPlayer}
              onClick={(pitInx) => this.props.play(this.state.gameId, viewer, pitInx)} 
              isWinner={data.status.statusType === 'FINISHED' ? this.getWinner(data) == side.sideId : false}/>
          <br/>
          </div>
        ))

      }     
        <GameStatus viewer={viewer} currentPlayer={data.status.currentPlayer} nextPlayer={data.status.nextPlayer} statusType={data.status.statusType} 
          winner={data.status.statusType === 'FINISHED' ? this.getWinner(data) : -1}/>
        <SockJsClient
                    url={ConfigService.getWebSocketUrl()}
                    topics={['/topic/kalah/' + data.id]}
                    onMessage={(gameEvent) => this.handleEvent(gameEvent)} />
      </div>
    );
  }

  createNewGameButton(props) {
    let open = false;
    return (
        <div>
          <br />
          <CreateGameDialog open={open} onCreated={() => {props.fetchGame(this.props.match.params.gameId); this.forceUpdate();}}/>
        </div>);
  }

  handleEvent(gameEvent) {
    let game = this.state.game;
    game.data.data = gameEvent.game;

    this.setState({
      game: game
    })
  }

  getWinner(data) { // TODO implement state of tie, what if no winners ?
    return data.board.sides.reduce((s1, s2) => (s1.house.numOfStones > s2.house.numOfStones ? s1 : s2)).sideId;
  }
}


const Pit = (props) => (
  <div className={props.type == 'HOUSE' ? 'house' : 'pit'} onClick={() => (props.onClick(props.index))}>
    <span>#{props.index} </span>
    <div> {props.numOfStones} </div>
  </div>
)

const Side = (props) => (
  <div id={props.id} className={ 'side' + (props.opposite ? ' opposite' : '') + (props.currentPlayer == props.id ? ' playing' : '') + (props.isWinner ? ' winner' : '') }>
      <span style={{ whiteSpace: 'nowrap' }}>Player #{props.id} </span>
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
    <br/>
    { props.winner > -1 &&
        <div>
          <MessagePrompter id='gameStatusMessage' open={true} 
            message={props.winner == props.viewer ? 'You win! Perfect!' : 'You lost :( The winner is: Player #' + props.winner}  type='success'/>
           {props.winner == props.viewer ? 'You win! Perfect!' : 'You lost :( The winner is: Player #' + props.winner} 
        </div>
    }
  </div>
)

Game.propTypes = {
};

export default connect(
    state => ({game: state.entities && state.entities.game}),
    { fetchGame, play }
)(Game);
