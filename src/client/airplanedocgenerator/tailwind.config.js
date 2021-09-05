const colors = require('tailwindcss/colors');

module.exports = {
    purge: [],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {
            colors: {
                accent: "#5E74D6",
                white: "#FFFFFF",
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
