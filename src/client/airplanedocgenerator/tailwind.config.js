module.exports = {
    purge: [],
    darkMode: false, // or 'media' or 'class'
    theme: {
        extend: {},
        boxShadow: {
            card: "6px 8px #5E74D6",
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
