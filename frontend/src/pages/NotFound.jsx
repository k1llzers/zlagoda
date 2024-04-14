import logo from '../logo.svg';
import '../App.css';

const NotFound = () => {
    return (
        <div className="App">
            <header className="App-header">
                <img src={logo} className="App-logo" alt="logo"/>
                <div className="App-intro">
                    <h2>Something went wrong</h2>
                    <h2><a href="/">Go home</a></h2>
                </div>
            </header>
        </div>
    );
};

export default NotFound;