import React from "react";

import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";

class SelectProjectItem extends React.Component {
    constructor(props) {
        super(props);
        
        this.nome = this.props.projectName;

        this.setSelected = this.setSelected.bind(this);
        this.delete = this.delete.bind(this);

    }

    setSelected() {
        this.props.setSelectedItem(this.nome);

    }

    delete(){
        this.props.deleteProject(this.nome);

    }

    getSelectItem(){
        let item = (
            <div className="flex flex-row justify-between pr-3 pl-3 border-b-2 border-gray-400 mb-5">
                <label className="hover:text-green-600 cursor-pointer" onClick={this.setSelected}>{this.nome}</label>
                <FontAwesomeIcon
                    className="cursor-pointer hover:text-red-400 active:text-red-600 focus:text-red-400"
                    icon={faTrash}
                    color={"#FF0000"}
                    onClick={this.delete}
                />
            </div>
        );

        return item;
    }

    render() {
        let item = this.getSelectItem();

        return item;
    }

}

export default SelectProjectItem;