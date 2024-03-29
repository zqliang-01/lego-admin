tinymce.PluginManager.add('rowspacing', function(editor, url) {
    var pluginName='设置段间距';
    var global$1 = tinymce.util.Tools.resolve('tinymce.util.Tools');
    var rowspacing_val = editor.getParam('rowspacing_val', '5px 10px 15px 20px 25px 30px');

    editor.on('init', function() {
        editor.formatter.register({
            rowspacingtop: {
                selector: 'p,h1,h2,h3,h4,h5,h6,td,th,div,ul,ol,li,table',
                styles: { 'margin-top': '%value' }
            },
            rowspacingbottom: {
                selector: 'p,h1,h2,h3,h4,h5,h6,td,th,div,ul,ol,li,table',
                styles: { 'margin-bottom': '%value' }
            }
        });
    });

    var doTopAct = function (value) {
        editor.formatter.apply('rowspacingtop', { value: value });
        editor.fire('change', {});
    };

    var doBottomAct = function (value) {
        editor.formatter.apply('rowspacingbottom', { value: value });
        editor.fire('change', {});
    };

    editor.ui.registry.getAll().icons.rowspacingtop || editor.ui.registry.addIcon('rowspacingtop','<svg t="1588054802814" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7912" xmlns:xlink="http://www.w3.org/1999/xlink" width="18" height="18"><defs><style type="text/css"></style></defs><path d="M894.29333333 493.72273778L129.70666667 493.72273778c-30.14656001 0-54.61333333-16.31118222-54.61333334-36.40888889s24.46677333-36.40888888 54.61333334-36.4088889l764.58666666 0c30.14656001 0 54.61333333 16.31118222 54.61333334 36.4088889s-24.46677333 36.40888888-54.61333334 36.40888889m-1.16508444 227.55555555l-764.58666667-1e-8c-29.70965333-0.36408889-53.44824889-16.52963555-53.44824889-36.40888887 0-19.80643555 23.73859555-35.97198222 53.44824889-36.40888891l764.58666667 1e-8c19.80643555-0.21845333 38.22933333 6.62641778 48.27818666 18.0588089a26.36003555 26.36003555 0 0 1 0 36.84579555c-10.04885333 11.35957333-28.47175111 18.20444445-48.27818666 17.91317333M917.95911112 948.90666667L106.62343111 948.90666667c-17.25781332 0-31.23882667-16.31118222-31.23882666-36.40888889s13.98101333-36.40888889 31.23882665-36.40888889l811.33568001 2e-8c17.25781332 0 31.23882667 16.31118222 31.23882667 36.40888887s-13.98101333 36.40888889-31.23882666 36.40888889M480.97962667 84.55964445L373.79185778 213.66556445c-16.16554667 19.6608-4.22343111 52.4288 19.22389333 52.42880001l214.22990222-1e-8c23.52014222 0 35.53507556-32.768 19.29671112-52.42879999L519.28177778 84.55964445a24.17550222 24.17550222 0 0 0-38.37496889 0" fill="#333333" p-id="7913"></path></svg>');
    editor.ui.registry.getAll().icons.rowspacingbottom || editor.ui.registry.addIcon('rowspacingbottom','<svg t="1588054797622" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="7792" xmlns:xlink="http://www.w3.org/1999/xlink" width="18" height="18"><defs><style type="text/css"></style></defs><path d="M129.70666667 530.27726222L894.29333333 530.27726222c30.14656001 0 54.61333333 16.31118222 54.61333334 36.40888889s-24.46677333 36.40888888-54.61333334 36.40888889l-764.58666666 0c-30.14656001 0-54.61333333-16.31118222-54.61333334-36.40888889s24.46677333-36.40888888 54.61333334-36.40888889m1.16508444-227.55555555l764.58666667 0c29.70965333 0.36408889 53.44824889 16.52963555 53.44824889 36.40888888 0 19.80643555-23.73859555 35.97198222-53.44824889 36.4088889l-764.58666667 0c-19.80643555 0.21845333-38.22933333-6.62641778-48.27818666-18.0588089a26.36003555 26.36003555 0 0 1 0-36.84579555c10.04885333-11.35957333 28.47175111-18.20444445 48.27818666-17.91317333M106.04088888 75.09333333L917.37656889 75.09333333c17.25781332 0 31.23882667 16.31118222 31.23882666 36.40888889s-13.98101333 36.40888889-31.23882666 36.40888889l-811.33568-1e-8c-17.25781332 0-31.23882667-16.31118222-31.23882667-36.40888888s13.98101333-36.40888889 31.23882666-36.40888889M543.02037333 939.44035555L650.20814222 810.33443555c16.16554667-19.6608 4.22343111-52.4288-19.22389333-52.4288l-214.22990222 0c-23.52014222 0-35.53507556 32.768-19.29671112 52.4288L504.71822222 939.44035555a24.17550222 24.17550222 0 0 0 38.37496889 0" fill="#333333" p-id="7793"></path></svg>');

    editor.ui.registry.addMenuButton('rowspacingtop', {
        icon: 'rowspacingtop',
        tooltip: pluginName,
        fetch: function(callback) {
            var dom = editor.dom;
            var blocks = editor.selection.getSelectedBlocks();
            var lhv = 0;
            global$1.each(blocks, function(block) {
                if(lhv==0){
                    lhv = dom.getStyle(block,'margin-top') ? dom.getStyle(block,'margin-top') : 0;
                }
            });

            var items = rowspacing_val.split(' ').map(function(item){
                var text = item;
                var value = item;
                return {
                    type: 'togglemenuitem',
                    text: text,
                    active : lhv==value ? true :false,
                    onAction: function() {
                        doTopAct(value);
                    }
                };
            });
            callback(items);
        }
    });

    editor.ui.registry.addMenuButton('rowspacingbottom', {
        icon: 'rowspacingbottom',
        tooltip: pluginName,
        fetch: function(callback) {
            var dom = editor.dom;
            var blocks = editor.selection.getSelectedBlocks();
            var lhv = 0;
            global$1.each(blocks, function(block) {
                if(lhv==0){
                    lhv = dom.getStyle(block,'margin-bottom') ? dom.getStyle(block,'margin-bottom') : 0;
                }
            });

            var items = rowspacing_val.split(' ').map(function(item){
                var text = item;
                var value = item;
                return {
                    type: 'togglemenuitem',
                    text: text,
                    active : lhv==value ? true :false,
                    onAction: function() {
                        doBottomAct(value);
                    }
                };
            });
            callback(items);
        }
    });

    return {
        getMetadata: function () {
            return  {
                name: pluginName
            };
        }
    };
});
