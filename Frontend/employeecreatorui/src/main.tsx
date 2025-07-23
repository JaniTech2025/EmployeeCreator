// import { Provider } from "./components/ui/provider"
import { ChakraProvider } from "@chakra-ui/react"
import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App"
import EmployeeProvider from "./context/EmployeeContext"

ReactDOM.createRoot(document.getElementById("root")!).render(
  <React.StrictMode>
    <ChakraProvider>
      <EmployeeProvider>
        <App />
      </EmployeeProvider>
    </ChakraProvider>
  </React.StrictMode>
)