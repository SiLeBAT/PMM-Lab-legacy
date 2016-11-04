fsk_editor = function () {

    var editor = { version: "0.0.1" };
    editor.name = "FSK Editor";

    var _val;
    var _rep;

    editor.init = function (representation, value)
    {
        _rep = representation;
        _val = value;

        create_body ();
    };

    editor.getComponentValue = function ()
    {
        return _val;
    };

    return editor;

    function create_body ()
    {
        document.createElement("body");
        $("body").html('<div class="container"></div>');
        $(".container").append(
            '<ul class="nav nav-pills">' +
            '  <li role="presentation" class="active"><a href="javascript:void(0)">Model</a></li>' +
            '  <li role="presentation"><a href="javascript:void(0)">Parameters</a></li>' +
            '  <li role="presentation"><a href="javascript:void(0)">Visualization</a></li>' +
            '</ul>');

        $(".container").append('<pre><code contenteditable></code></pre>');
        $(".container pre code").text(_val.modelScript);

        $(".container ul li:eq(0) a").click(function () { tab_event("model"); });
        $(".container ul li:eq(1) a").click(function () { tab_event("param"); });
        $(".container ul li:eq(2) a").click(function () { tab_event("viz"); });

        $(".container").append('<div><input class="btn btn-danger" type="submit" value="Save"></div>');
        $(".btn").click(function () {

            // Get index of active tab:
            // 0 (model), 1 (param) or 2 (viz)
            var activeTab = $(".active");
            var activeTabName = $("a", activeTab).text();

            if (activeTabName === "Model") {
                _val.modelScript = $(".container pre code").text();
            } else if (activeTabName === "Parameters") {
                _val.paramScript = $(".container pre code").text();
            } else if (activeTabName === "Visualization") {
                _val.vizScript = $(".container pre code").text();
            }
        });
    }

    /**
     * Event of selecting a tab.
     * The code is updated to follow the selected tab and the selected tab is
     * marked as actived.
     * 
     * scriptType may be "model", "param" or "viz".
     */
    function tab_event(scriptType) {

        var modelButton = $(".container ul li:eq(0) a");
        var paramButton = $(".container ul li:eq(1) a");
        var vizButton = $(".container ul li:eq(2) a");

        // Remove active class to the button that was previously actived
        $(".active").removeClass("active");

        if (scriptType === "model") {
            // modelButton.addClass("active");
            $(".container ul li:eq(0)").addClass("active");
            $(".container pre code").text(_val.modelScript);
        } else if (scriptType === "param") {
            // paramButton.addClass("active");
            $(".container ul li:eq(1)").addClass("active");
            $(".container pre code").text(_val.paramScript);
        } else if (scriptType === "viz") {
            // vizButton.addClass("active");
            $(".container ul li:eq(2)").addClass("active");
            $(".container pre code").text(_val.vizScript);
        }
    }
}();
