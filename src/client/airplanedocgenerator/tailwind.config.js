module.exports = {
    purge: [],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            blur: {
                blurLogin: "2px"
            },
            boxShadow: {
                simpleShadow: "3px 2px 3px rgba(0, 0, 0, 0.25)",
                loginFormShadow: "10px 10px 0px -2px rgba(102,126,234,0.5)",
                topBarShadow: "0px 2px 4px rgba(0, 0, 0, 0.2)"
            },
            width: {
              loginFormW: "300px"
            },
            height:{
                loginFormH: "400px"
            },
            colors: {
                accent: "#5E74D6",
                white: "#FFFFFF",
                loginIcon: "#5E74D6"
            },
            backgroundColor: {
                topBar: "#5E74D6",
                hoverTopBarButton: "#8498f0",
                activeTopBarButton: "#acbbff"
            },
            fontSize: {
                "20px": "20px"
            }
        },
        boxShadow: {
            card: "6px 8px #5E74D6",
            lg: "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)",
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
            backgroundColor: ["active"]
        },
    },
    plugins: [],
};
