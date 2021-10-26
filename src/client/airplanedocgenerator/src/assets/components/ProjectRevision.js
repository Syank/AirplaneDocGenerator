import React from "react";

class ProjectRevision extends React.Component {
    constructor(props) {
        super(props);

        this.projectName = this.props["projectName"];
        this.id = "revisionScreen";

        this.close = this.close.bind(this);
        this.getRevisionsScreen = this.getRevisionsScreen.bind(this);
        this.getRevisionsList = this.getRevisionsList.bind(this);
        this.getRevisionElements = this.getRevisionElements.bind(this);

        this.state = {
            projectData: this.props["projectData"]
        }

    }

    close(event){
        let clickTargetId = event.target.id;

        if (clickTargetId === this.id) {
            this.props.hide();

        }

    }

    getRevisionDate(dateFragment){
        let year = dateFragment[0];
        let month = dateFragment[1];
        let day = dateFragment[2];

        let dateString = day + "/" + month + "/" + year;

        return dateString;
    }

    getRevisionElements(){
        let revisionsData = this.state["projectData"]["revisions"];

        let lines = [];

        for (let i = 0; i < revisionsData.length; i++) {
            const revisionData = revisionsData[i];
            
            let version = revisionData["version"];
            let description = revisionData["description"];
            let date = this.getRevisionDate(revisionData["creationDate"]);

            let bgColor = "bg-blue-100";

            if(i % 2 === 0){
                bgColor = "bg-blue-200";
            }

            let component = (
                <div key={"revision-" + version} className={"w-full pb-5 text-center hover:bg-blue-300 bg-opacity-50 " + bgColor}>
                    <div className="w-full h-full flex flex-row justify-between pr-5 pl-5">
                        <label className="font-bold">{"Revisão " + version}</label>
                        <label className="font-bold">{"Data de revisão: " + date}</label>
                    </div>
                    <p>
                        {description}
                    </p>
                </div>
            );

            lines.push(component);

        }

        console.log(revisionsData);

        return lines;
    }

    getRevisionsList(){
        let elements = this.getRevisionElements();

        let component = (
            <div className="h-full w-full overflow-auto">
                {elements}
            </div>
        );

        return component;
    }

    getRevisionsScreen() {
        let component = (
            <div id={this.id} className="z-10 w-full h-full absolute flex flex-row items-center justify-center backdrop-filter backdrop-blur-blurLogin" onClick={this.close}>
                <div className="h-4/6 w-4/6 flex flex-col items-center justify-center bg-white shadow-registerUser">
                    <div className="w-5/6 mt-5 text-center border-b-2 border-opacity-50 border-black mb-5">
                        <h1 className="text-2xl	font-bold text-center leading-loose">
                            Revisões do projeto
                        </h1>
                        <p>
                            Abaixo está a lista de revisões feitas para esse projeto e suas descrições
                        </p>
                    </div>
                    {this.getRevisionsList()}
                </div>
            </div>
        );

        return component;
    }

    render() {
        let revisionScreen = this.getRevisionsScreen();

        return revisionScreen;
    }

}

export default ProjectRevision;