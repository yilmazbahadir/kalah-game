import React from 'react';
import PropTypes from 'prop-types';
import { withStyles } from '@material-ui/core/styles';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemSecondaryAction from '@material-ui/core/ListItemSecondaryAction';
import ListItemText from '@material-ui/core/ListItemText';
import { fetchGameList } from '../../redux/actions/thunks';
import { connect } from 'react-redux';
import Tooltip from '@material-ui/core/Tooltip';
import CreateGameDialog from './CreateGameDialog';
import JoinGame from './JoinGame';
import Icon from './../common/Icon';

const styles = theme => ({
  root: {
    width: '100%',
    maxWidth: 700,
    backgroundColor: theme.palette.background.paper,
  },
  button: {
    margin: theme.spacing.unit,
  },
  leftIcon: {
    marginRight: theme.spacing.unit,
  },
  rightIcon: {
    marginLeft: theme.spacing.unit,
  },
  iconSmall: {
    fontSize: 20,
  },
});

class GameList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        games: {data: [], error:null, isFetching: true}
      };
  }
    
  componentDidMount() {
    this.props.fetchGameList();
  } 

  componentWillReceiveProps(nextProps) {
    this.setState({
        games: nextProps.games
    })
}
  render() {
    if (!this.state.games) return <span />;

    const { error, data, isFetching } = this.state.games;
    let content = null;

    if (isFetching) {
        content = <span>Loading!</span>;
    } else if (error) {
        content =  <span>{ error.message }</span>
    } else if(data.data && data.data.length==0) {
        content = (<div><span>No games found!</span>
        {this.createNewGameButton(this.props)}</div>);   
    } else {
        const { classes } = this.props;
    
        content =  (
        
        <div className={classes.root}>
            <div>
                <h1 className="title">
                    <Icon icon="sitemap" className="has-text-info" prefix="fas" />&nbsp;Kalah Game List
                </h1>
                <h3 className="subtitle ">
                    <Icon icon="angle-right"/>&nbsp;
                    List of the games
                </h3>
            </div>
            <List >
            { 
                data.data.map(value => (
                <Tooltip key={value.id} title={this.listAllPlayers(value.players)} placement="right-start">
                    <ListItem
                    key={value.id}
                    role={undefined}
                    dense
                    button
                    className={classes.listItem}
                    >
                        <ListItemText primary={value.name} />
                        <ListItemText primary={value.players.length +  '/' + value.config.numOfPlayers + ' Players'} />
                        <ListItemText primary={value.status.statusType} />
                        <ListItemSecondaryAction>
                            <JoinGame gameId={value.id} open={false}/>
                        </ListItemSecondaryAction>
                    </ListItem>
                </Tooltip>    

                
            ))}
            </List>
            {this.createNewGameButton(this.props)} 
            
        </div>
        );
    }
    
    return content;
  }

  listAllPlayers(players) {
    if(players && players[0]) {
        return 'Players: ' + players.map(p => p.name).reduce((f,s) => f + ', ' + s);
    }
    return 'Players: none'
  }

  createNewGameButton(props) {
    let open = false;
    return (
        <div>
            <br/>
            <CreateGameDialog open={open} onCreated={() => {props.fetchGameList();}}/>
        </div>);
  }

}


GameList.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default connect(
    state => ({games: state.entities && state.entities.games}),
    { fetchGameList }
)(withStyles(styles)(GameList));
