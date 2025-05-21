/*
    Method for retrieving the individual data according to the task description.
    @author Giuseppe Abrami
 */
/**
 * Method for retrieving the speaker information
 * @param collection
 * @param RedeMongoID
 */
function loadNamedEntityData(RedeMongoID) {

    $.ajax({
        url: "/NamedEntityData/" + RedeMongoID,
        type: 'GET',
        async: true,
        success: function (a, b, c) {
            console.log(a);
            // The result of the query is transferred to the visualization, including data mapping.
            chart = BubbleChart(a, {
                label: d => d.label,
                value: d => d.value,
                group: d => d.label,
                title: d => d.label + ": " + d.value,
                width: 1000
            })

            $("#BubbleChart"+RedeMongoID).append(chart);

        },
        failure: function (xhr, text, error) {
            console.log(text);
            console.log(error);
        }
    });
}

function loadPOSData(RedeMongoID) {

    $.ajax({
        url: "/POSData/" + RedeMongoID,
        type: 'GET',
        async: true,
        success: function (a, b, c) {
            console.log(a);
            // The result of the query is transferred to the visualization, including data mapping.
            chart = PieChart(a, {
                name: d => d.label,
                value: d => d.value,
                title: d => d.label + ": " + d.value,
                width: 1000,
                height: 720
            })

            $("#PieChart"+RedeMongoID).append(chart);

        },
        failure: function (xhr, text, error) {
            console.log(text);
            console.log(error);
        }
    });
}


function loadNamedEntityDataEvaluation(RedeMongoID) {

    $.ajax({
        url: "/NamedEntityDataEvaluation/" + RedeMongoID,
        type: 'GET',
        async: true,
        success: function (a, b, c) {
            console.log(a);
            // The result of the query is transferred to the visualization, including data mapping.
            chart = BubbleChart(a, {
                label: d => d.label,
                value: d => d.value,
                group: d => d.label,
                title: d => d.label + ": " + d.value,
                width: 1000
            })

            $("#BubbleChart"+RedeMongoID).append(chart);

        },
        failure: function (xhr, text, error) {
            console.log(text);
            console.log(error);
        }
    });
}

function loadPOSDataEvaluation(RedeMongoID) {

    $.ajax({
        url: "/POSDataEvaluation/" + RedeMongoID,
        type: 'GET',
        async: true,
        success: function (a, b, c) {
            console.log(a);
            // The result of the query is transferred to the visualization, including data mapping.
            chart = PieChart(a, {
                name: d => d.label,
                value: d => d.value,
                title: d => d.label + ": " + d.value,
                width: 1000,
                height: 720
            })

            $("#PieChart"+RedeMongoID).append(chart);

        },
        failure: function (xhr, text, error) {
            console.log(text);
            console.log(error);
        }
    });
}
