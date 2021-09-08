import React from "react";

class CardHeader extends React.Component {
    render() {
        return (
            <div className="mt-4 mx-auto w-5/12">
                <div className="shadow-lg">
                    <div className="card m-2 shadow-card border border-white text-center bg-white">
                        <div className="m-3 card">
                            <h2 className="font-bold mb-2 text-accent text-3xl pt-4">
                                {this.props.title}
                            </h2>
                            <p className="text-sm p-6">
                                {this.props.description}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default CardHeader;
