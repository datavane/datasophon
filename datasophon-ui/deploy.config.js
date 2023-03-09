const PATH = '../datasophon-api/src/main/resources/front/'

module.exports = [
  {
    source: './dist',
    clean: true,
    target: `${PATH}/static/resources/bundle-main`,
  },
  {
    source: './dist/index.html',
    target: `${PATH}/views/index.html`,
    replace: [{ pattern: /\/static\//g, replacement: '/static/' }],
  },
]
