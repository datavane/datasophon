const PATH = '../datasophon-api/src/main/resources/front/'

module.exports = [
    {
        source: './dist',
        clean: true,
        target: `${PATH}/static/resources/bundle-main`
    },
    {
        source: './dist/index.html',
        target: `${PATH}/views/index.html`,
        replace: [
            {pattern: /\/\$\{path\}/g, replacement: '/ddh/resources/bundle-main'},
            {pattern: /\.\/oden-sql-editor\//g, replacement: '/ddh/resources/bundle-main/oden-sql-editor/'},
            {pattern: 'window.FRONTONLY = true;', replacement: ''},
            {
                pattern: '<x-insert name=flowidepath>',
                replacement: '<script type="text/javascript">window.FLOWIDEURL = "${flowideUrl}";window.CONTEXTPATH = ""</script>'
            },
            {pattern: '<x-insert name=taglibs>', replacement: ''},
        ]
    },
    {
        source: './dist/errorLicense.html',
        target: `${PATH}/views/errorLicense.html`,
        replace: []
    },

    {
        source: './dist/advancedCron/cron-dialog.html',
        target: `${PATH}/views/advancedCron/cron-dialog.html`,
        replace: [
            {pattern: /\/idecomponent\//g, replacement: '/ddh/resources/bundle-main/idecomponent/'},
            {
                pattern: '<x-insert name=flowidepath>',
                replacement: '<script type="text/javascript">window.FLOWIDEURL = "${flowideUrl}";window.CONTEXTPATH = ""</script>'
            },
            {pattern: '<x-insert name=taglibs>', replacement: '<@h.headvue />'}
        ]
    }
]
