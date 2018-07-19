import React from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import DialogTitle from '@material-ui/core/DialogTitle';
import IconButton from '@material-ui/core/IconButton';
import PlayArrow from '@material-ui/icons/PlayArrow';

import { joinGame } from '../../redux/actions/thunks';
import { connect } from 'react-redux';
import { Route } from 'react-router-dom';

let hist = null;

class JoinGameDialog extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      open: this.props.open,
      gameId:this.props.gameId,
      playerName:'',
      player: {
        id:0,
        name:''
      }
    };
  }

  componentDidUpdate() {
    
  }

  handleClickOpen = () => {
    this.setState({ open: true });
  };

  handleClose = () => {
    this.setState({ open: false });
  };

  handleJoinGame = (history) => {
    this.props.joinGame(this.props.gameId, this.state.playerName, 
      (dispatch, data) => { history.push('/kalah/game/' + this.props.gameId + '/player/' + data.data.id); this.handleClose()});
  }

  onChange = (e) => {
    this.setState({ [e.target.id]: e.target.value });
  }

  render() {
    return (
      <div>
        <IconButton aria-label="Join Game" onClick={this.handleClickOpen}>
                    <PlayArrow />
                    </IconButton>
        <Dialog
          open={this.state.open}
          onClose={this.handleClose}
          aria-labelledby="form-dialog-title"
        >
          <DialogTitle id="form-dialog-title">Join Game</DialogTitle>
          <DialogContent>
            <DialogContentText>
              Game Id : {this.state.gameId}
            </DialogContentText>
            <TextField
              autoFocus
              margin="dense"
              id="playerName"
              label="Player Name"
              type="text"
              value={this.state.playerName}
              onChange={this.onChange}
              fullWidth
            />
           
          </DialogContent>
          <DialogActions>
            <Button onClick={this.handleClose} color="secondary">
              Cancel
            </Button>
            <Route render={({ history}) => (
               <Button onClick={() => this.handleJoinGame(history)} color="primary">
                {this.props.player && this.props.player.isFetching ? 'Joining': 'Join'}
              </Button>)} />
            
          </DialogActions>
        </Dialog>
      </div>
    );
  }
}

export default connect(
  state => ({player: state.entities && state.entities.player}),
  { joinGame }
)(JoinGameDialog);