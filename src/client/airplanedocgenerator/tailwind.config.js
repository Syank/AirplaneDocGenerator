module.exports = {
    purge: [],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {},
        boxShadow: {
            card: "6px 8px #5E74D6",
            lg: "0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05)",
        },
        fontFamily: {
            roboto: ["Roboto"],
        },
        colors: {
            accent: "#5E74D6",
            white: "#FFFFFF",
        },
        borderColor: (theme) => ({
            ...theme("colors"),
            DEFAULT: theme("colors.gray.300", "currentColor"),
            white: "#FFFFFF",
        }),
    },
    variants: {
        extend: {},
    },
    plugins: [],
};
