import TopBar from "./assets/components/TopBar";
import HomeScreen from "./views/HomeScreen";

import LoginScreen from "./views/LoginScreen";

function App() {
    // Previne o aparecimento das barras de rolagem
    document.body.style.overflow = "hidden";

    return (
        <div id="App" className="App w-screen h-screen flex flex-col overflow-hidden">
            <TopBar></TopBar>
            
            <LoginScreen></LoginScreen>
        </div>
    );
}

export default App;
