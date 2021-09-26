import React from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faQuestionCircle } from "@fortawesome/free-solid-svg-icons";

/**
 * Componente tooltip
 * @author Carolina Margiotti
 */
class Tooltip extends React.Component {
    constructor(props) {
        super(props);

        this.text = this.props.text;

        this.state = {
            mode: "hide",
        };

        this.handleMouseEnter = this.handleMouseEnter.bind(this);
        this.handleMouseLeave = this.handleMouseLeave.bind(this);
    }

    /**
     * Função faz o tooltip aparecer quando mouse passa em cima
     * @author Carolina Margiotti
     */
    handleMouseEnter() {
        this.setState({ mode: "view" });
    }

    /**
     * Função faz o tooltip desaparecer quando mouse passa em cima
     * @author Carolina Margiotti
     */
    handleMouseLeave() {
        this.setState({ mode: "hide" });
    }

    /**
     * Renderiza o tooltip
     * @returns Elemento a ser renderizado
     * @author Carolina Margiotti
     */
    render() {
        if (this.state.mode === "view") {
            return (
                <div className="group cursor-help inline-block relative">
                    <div className="absolute z-10 w-36 text-center bottom-full left-1/2 transform -translate-x-1/2">
                        <div className="relative mx-2">
                            <div className="bg-black text-white text-xs rounded py-1 px-4 right-0 bottom-full">
                                {this.text}
                                <svg
                                    className="absolute h-3 w-full left-0 top-full"
                                    viewBox="0 0 255 255"
                                >
                                    <polygon points="0,0 127.5,127.5 255,0" />
                                </svg>
                            </div>
                        </div>
                    </div>
                    <FontAwesomeIcon
                        icon={faQuestionCircle}
                        color="black"
                        onMouseLeave={this.handleMouseLeave}
                    />
                </div>
            );
        } else {
            return (
                <FontAwesomeIcon
                    icon={faQuestionCircle}
                    color="black"
                    onMouseEnter={this.handleMouseEnter}
                />
            );
        }
    }
}

export default Tooltip;
