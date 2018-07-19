import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';

import { createGame } from '../../redux/actions/thunks';
import { connect } from 'react-redux';
import { Route } from 'react-router-dom';

class CreateGameDialog extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      open: this.props.open,
      gameConfig: {
        name:'game',
        numOfPlayers: 2,
        numOfPits: 6,
        numOfStonesInEachPit:6
      }
    };
  }
  handleClickOpen = () => {
    this.setState({ open: true });
  };

  handleClose = () => {
    this.setState({ open: false });
  };

  handleCreate = () => {
    this.props.createGame(this.state.gameConfig, () => {this.props.onCreated(); this.handleClose()});
  }

  handleCreateAndJoin = (history) => {
    this.props.createGame(this.state.gameConfig, 
      (dispatch, data) => { history.push('/kalah/game/' + this.props.gameId + '/join')});
  }
  onChange = (e) => {
    this.setState({ gameConfig: {...this.state.gameConfig, [e.target.id]: e.target.value} });
  }

  render() {
    return (
      <div>
        {/*<Button variant="contained" color="primary" onClick={() => open=true}>
             New Game
            <Icon className={classNames(classes.leftIcon, classes.iconSmall)}>add</Icon>
        </Button>*/}
        <Button onClick={this.handleClickOpen}>New Game</Button>
        <Dialog
          open={this.state.open}
          onClose={this.handleClose}
          aria-labelledby="form-dialog-title"
        >
          <DialogTitle id="form-dialog-title">Create a New Kalah Game</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Kalah Game is a multi player game. It can be constructed as 2n players(sides), n numbler of pits in each side, n stones in each pit.
            </DialogContentText>
            <TextField
              autoFocus
              margin="dense"
              id="name"
              label="Name of the Game"
              type="text"
              value={this.state.gameConfig.name}
              onChange={this.onChange}
              fullWidth
            />
            <TextField
              margin="dense"
              id="numOfPlayers"
              label="Number of players(sides)"
              type="number"
              inputProps={{ min: "2", max: "8", step: "2" }}
              value={this.state.gameConfig.numOfPlayers}
              onChange={this.onChange}
              fullWidth
            />
            <TextField
              margin="dense"
              id="numOfPits"
              label="Number of pits in each side"
              type="number"
              inputProps={{ min: "6", max: "10", step: "1" }}
              value={this.state.gameConfig.numOfPits}
              onChange={this.onChange}
              fullWidth
            />
            <TextField
              margin="dense"
              id="numOfStonesInEachPit"
              label="Number of stones in each pit"
              type="number"
              inputProps={{ min: "4", max: "10", step: "1" }}
              value={this.state.gameConfig.numOfStonesInEachPit}
              onChange={this.onChange}
              fullWidth
            />
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color="secondary">
              Cancel
            </Button>
            <Button onClick={this.handleCreate} color="primary">
              Create
            </Button>
            <Route render={({ history}) => (
              <Button disabled onClick={() => this.handleCreateAndJoin(history)} color="primary">
               Create & Join
             </Button>)} />
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}

export default connect(
  state => ({game: state.entities && state.entities.game}),
  { createGame }
)(CreateGameDialog);