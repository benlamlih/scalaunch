/** @type {import('tailwindcss').Config} */
module.exports = {
  mode: 'jit',
  content: [
    './index.html',
    './src/main/scala/**/*.scala',
    './src/main/css/tailwind.css',
    './target/scala-3.3.4/**/*.js',
  ],
  theme: {
    extend: {},
  },
  plugins: [],
};
