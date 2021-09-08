import React from "react";

class CardHeader extends React.Component {
    render() {
        return (
            <div class="container mt-4 mx-auto">
                <div class="grid grid-cols-3">
                    <div class="shadow-lg cursor-pointer transform hover:-translate-y-1 transition-all duration-200">
                        <div class="shadow-card bg-white ">
                            <div class="card m-2  border border-white text-center">
                                <div class="m-3 card">
                                    <h2 class="font-roboto font-bold text-lg mb-2 text-accent text-3xl pt-4">
                                        {this.props.title}
                                    </h2>
                                    <p class="font-roboto text-sm underline font-bold p-6">
                                        {this.props.description}
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default CardHeader;
