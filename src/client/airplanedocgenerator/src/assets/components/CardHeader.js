import React from "react";

class CardHeader extends React.Component {
    render() {
        return (
            <div class="container mt-4 mx-auto ">
                <div class=" grid grid-cols-3 ">
                    <div class="card m-2 shadow-card cursor-pointer border border-white text-center bg-white transform hover:-translate-y-1 transition-all duration-200">
                        <div class="m-3">
                            <h2 class="font-roboto font-bold text-lg mb-2 text-accent text-3xl pt-4">
                                Criação de manual
                            </h2>
                            <p class="font-roboto text-sm underline font-bold p-6">
                                Aqui é possível criar um novo projeto de manual,
                                seja seguindo o guia para criar um do zero ou
                                fazendo a importação de uma estrutura válida
                                direto de sua máquina
                            </p>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default CardHeader;
