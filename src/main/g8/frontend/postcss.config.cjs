const tailwindcss = require('./tailwind.config.cjs');
const autoprefixer = require('autoprefixer');
const postcssImport = require('postcss-import');

module.exports = () => {
  const plugins = {
    'postcss-import': {},
    tailwindcss,
    autoprefixer: {},
  };
  return {
    plugins,
  };
};
