import React from "react";
import "../style/Loader.css";

class Loader extends React.Component {
    render() {
        return (
            <div>
                <div class="loader" id="loader-2">
                    <span></span>
                    <span></span>
                    <span></span>
                </div>
            </div>
        );
    }
}

export default Loader;
