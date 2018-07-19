import React from 'react';
import { withRouter } from 'react-router-dom';

import Icon from './common/Icon';
import Navbar from './common/bulma/Navbar';
import Footer from './Footer';
import Hero from './common/bulma/Hero';
import Columns from './common/bulma/Columns';
import Flex from './common/glamorous/Flex';
import GameList from './game/GameList';
import Routes from './Routes';


function App ({ location, history }) {
    return (
        <Flex column height="100%" width="100%" justifyContent="space-between">
            <div>
                { _renderHeader() }
            </div>
            <div>
                { _renderBody(location, history) }
            </div>
            <div>
                { _renderFooter() }
            </div>
        </Flex>
    );
}

function _renderHeader () {
    return (
        <Navbar brand={{
            icon : 'cloud',
            url  : 'http://www.github.com/yilmazbahadir/kalah-game',
            label: 'Kalah Game'
        }}/>
    );
}

function _renderBody (location, history) {
    return (
        <Hero
            content={(
                <div>
                    

                    { /* Render router example */ }
                    <Columns
                        columns={[
                            (
                               <Routes />
                            )]}
                    />

                    
                </div>
            )}
        />
    );
}

function _renderFooter () {
    return <Footer />;
}


export default withRouter(App);
