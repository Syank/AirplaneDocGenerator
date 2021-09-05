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
