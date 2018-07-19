import React from 'react';
import Icon from './common/Icon';
import Flex from './common/glamorous/Flex';

function Footer () {
    return (
        <footer className="footer has-text-grey-light">
            <div className="container">
                <div className="content has-text-centered">
                    <p>
                        <strong className="has-text-grey-light">
                            <Icon icon="cloud" />
                            &nbsp;Kalah Game
                        </strong> by <a href="https://github.com/yilmazbahadir" target="_blank" rel="noopener noreferrer">Bahadir Yilmaz</a>.
                        <br />
                        The <a href="https://github.com/yilmazbahadir/kalah-game" target="_blank" rel="noopener noreferrer">source code</a> is licensed under <a href="https://github.com/yilmazbahadir/kalah-game/blob/master/LICENSE">MIT</a>.
                    </p>
                    <Flex hAlignCenter>
                        <GithubButton
                            label="Star"
                            icon="octicon-star"
                            href="yilmazbahadir/kalah-game"
                            ariaLabel="Star yilmazbahadir/kalah-game on GitHub"
                        />
                        <GithubButton
                            label="Fork"
                            icon="octicon-repo-forked"
                            href="yilmazbahadir/kalah-game/fork"
                            ariaLabel="Fork yilmazbahadir/kalah-game on GitHub"
                        />
                        <GithubButton
                            label="Watch"
                            icon="octicon-eye"
                            href="yilmazbahadir/kalah-game/subscription"
                            ariaLabel="Watch yilmazbahadir/kalah-game on GitHub"
                        />
                    </Flex>
                </div>

            </div>
        </footer>
    );
}

function GithubButton ({
    label,
    icon,
    href,
    ariaLabel
}) {
    return (
        <div style={{ margin: 5 }}>
            <a className="github-button"
                href={`https://github.com/${href}`}
                data-icon={icon}
                data-size="large"
                data-show-count={true}
                aria-label={ariaLabel}>
                { label }
            </a>
        </div>
    );
}

export default Footer;
