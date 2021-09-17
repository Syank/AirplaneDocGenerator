module.exports = {
    purge: [],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            blur: {
                blurLogin: "2px",
            },
            boxShadow: {
                simpleShadow: "3px 2px rgba(0, 0, 0, 0.25)",
                loginFormShadow: "10px 10px 0px -2px rgba(102,126,234,0.5)",
                topBarShadow: "0px 2px 4px rgba(0, 0, 0, 0.2)",
                card: "6px 8px #5E74D6",
                registerUser: "0px 0px 30px 20px rgba(0,0,0,0.2);"
            },
            width: {
                loginFormW: "300px",
                topBarMenuW: "160px",
                selectProjectW: "650px"
            },
            height: {
                loginFormH: "400px",
                topBarMenuH: "fit-content",
                selectProjectH: "360px"
            },
            minHeight: {
                topBarMenuMinH: "40px"
            },
            colors: {
                accent: "#5E74D6",
                white: "#FFFFFF",
                loginIcon: "#5E74D6",
                inputFileColor: "#33AEB6"
            },
            backgroundColor: {
                topBar: "#5E74D6",
                hoverTopBarButton: "#8498f0",
                activeTopBarButton: "#acbbff",
                topBarMenu: "#5366bd",
                topBarMenuHover: "#5e96d6"
            },
            fontSize: {
                "20px": "20px",
            },
        },
        fontFamily: {
            roboto: ["Roboto"],
        },
        borderColor: (theme) => ({
            ...theme("colors"),
            DEFAULT: theme("colors.gray.300", "currentColor"),
            white: "#FFFFFF",
        }),
    },
    variants: {
        extend: {
            backgroundColor: ["active"],
        },
    },
    plugins: [],
};
