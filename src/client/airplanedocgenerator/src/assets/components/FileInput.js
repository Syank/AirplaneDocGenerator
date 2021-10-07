import React from "react";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCloudDownloadAlt } from '@fortawesome/free-solid-svg-icons';


class FileInput extends React.Component{
    constructor(props){
        super(props);

        this.accept=this.props.accept;

        this.accentColor = "#5E74D6";
        this.iconColor = "#FFFF"
        this.state = {
            fileSelected :{name:'Nenhum arquivo selecionado'}
        }

        this.selectedFileId= "selectedFileId";

        this.fileSelected = this.fileSelected.bind(this);

    }
    getComponent(){
        let component=(
        <div>
            <div className=" text-accent hover:text-white hover:color-white">
                <label class="w-64 flex flex-col items-center px-4 py-6 bg-white rounded-md shadow-md tracking-wide uppercase border border-blue cursor-pointer hover:bg-accent hover:text-white text-accent ease-linear transition-all duration-150">
                <FontAwesomeIcon icon={faCloudDownloadAlt} className="color-accent hover:color-white"/>
                        <span class="mt-2 leading-normal">Select a file</span>
                        <input id={this.selectedFileId} type='file' class="hidden" accept={this.accept} onChange={this.fileSelected} />
                    </label>
                <label id="fileName" className="w-64 text-accent truncate absolute">{this.state.fileSelected.name}</label>
            </div>
        </div>);

        return component;
    }

    fileSelected(value){
        let name = document.getElementById(this.selectedFileId);
        this.setState({fileSelected:name.files.item(0)})
    }

    render(){
        let fileInput=this.getComponent();
        return fileInput;
    }
}

export default FileInput;