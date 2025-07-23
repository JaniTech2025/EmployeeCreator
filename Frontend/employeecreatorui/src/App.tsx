import { useState, useContext } from 'react';

import './App.css'
// import { EmployeeForm } from './components/CreateEmployee/CreateEmployee'
import { ViewEmployees } from './components/ViewEmployee';

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
    <ViewEmployees/>
    </>
  )
}

export default App
