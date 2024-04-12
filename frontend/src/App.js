import logo from './logo.svg';
import './App.css';
import {Component, useEffect, useState} from "react";

function App() {
  const [employee, setEmployee] = useState([]);

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch('http://localhost:8080/api/employee');
                // if (!response.ok) {
                //     throw new Error('Network response was not ok');
                // }
                const body = await response.json();
                setEmployee(body);
            } catch (error) {
                console.error('Error fetching data:', error);
            }
        }
        fetchData();
    }, []);

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <div className="App-intro">
            <h2>Employees</h2>
            {employee.map(empl =>
                <div key={empl.id}>
                  {empl.name} ({empl.surname})
                </div>
            )}
          </div>
        </header>
      </div>
  );
}

// class App extends Component {
//     state = {
//         employee: []
//     };
//
//     async componentDidMount() {
//         const response = await fetch('http://localhost:8080/api/employee');
//         const body = await response.json();
//         console.log(body)
//         this.setState({employee: body});
//     }
//
//     render() {
//         const {employee} = this.state;
//         return (
//             <div className="App">
//                 <header className="App-header">
//                     <img src={logo} className="App-logo" alt="logo" />
//                     <div className="App-intro">
//                         <h2>Clients</h2>
//                         {employee.map(empl =>
//                             <div key={empl.id}>
//                                 {empl.name} ({empl.surname})
//                             </div>
//                         )}
//                     </div>
//                 </header>
//             </div>
//         );
//     }
// }

export default App;
