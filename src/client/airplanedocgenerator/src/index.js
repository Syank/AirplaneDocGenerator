import React from "react";
import ReactDOM from "react-dom";
import "./assets/style/index.css";
import reportWebVitals from "./utils/reportWebVitals";
import Application from "./Application";
import CreationScreen from "./views/CreationScreen";
import NewProjectScreen from "./views/NewProjectScreen";

// Previne o aparecimento das barras de rolagem
document.body.style.overflow = "hidden";

ReactDOM.render(
    <React.StrictMode>
        <NewProjectScreen/>
    </React.StrictMode>,
    document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
