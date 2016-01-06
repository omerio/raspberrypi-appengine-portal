/**
 * document onload
 */
$(function () {
  // toastr configurations
	toastr.options.extendedTimeOut = 1000;
	toastr.options.timeOut = 1000;
	toastr.options.fadeOut = 250;
	toastr.options.fadeIn = 250;

  if(window.location.hostname === 'localhost' && window.goog) {
    // slow down the rate of channel requests on the dev server
    goog.appengine.Socket.POLLING_TIMEOUT_MS = 20000;
  }

	app.refreshChart = $('#refresh-charts').prop('checked');

  $('#submit').click(function(e) {
    app.loadData(false);
    app.refreshChart = $('#refresh-charts').prop('checked');
    e.preventDefault();
  });

  // load the data
  app.loadData();

});



/**
 * App namespace
 */
window.app = window.app || {};

app.dateFormat = "MMM D, YYYY h:m:s a";

app.refreshChart = false;

/**
 * Load the initial data
 */
app.loadData = function(initChannel) {

  // by default we are initializing the channel
  if(typeof initChannel === 'undefined') {
    initChannel = true;
  }

  $('#submit').prop('disabled', true);

  $.ajax({
	  url: "/getdata",
	  data: {
      'timeRange': $('#time-range').val()
    },
	  type: "GET",
	  success: app.handleLoadSuccess.bind(this, initChannel),
	  error: function() {
		  toastr.error("Failed to load sensor data");
	  },
	  complete: function() {
		  $('#submit').prop('disabled', false);
	  }
  });
}

/**/

/**
 * Handle the response from the server. The data should look something like this:
 * {
 *   "data":[{"id":4855443348258816,"dateTime":"Jan 4, 2016 2:04:54 PM","value":10.832920314822168,"channel":"Temperature"},
 *     {"id":4679521487814656,"dateTime":"Jan 4, 2016 2:07:02 PM","value":20.51223558504378,"channel":"Temperature"}
 *   ],
 *   "clientId":"wlwtcqwbj7o6"
 * }
 * @param  {[type]} data - sensory data and channel token
 */
app.handleLoadSuccess = function(initChannel, data) {

	// first time only
  if(!app.token) {
  	toastr.success("Sensor data loaded successfully");
	}

  var sensorsData = data.data;
  // in the future this could be a dynamic list
  var dataMap = {
    'temperature': {
      'data': [],
      'color': '#FF9900',
      'label': 'Temperature'
    },
    'voltage': {
      'data': [],
      'color': '#66AA00',
      'label': 'Voltage'
    },
    'illuminance': {
      'data': [],
      'color': '#0099C6',
      'label': 'Illuminance'
    },
  };

  //separate and transform the data, the final array should look something like this
  // [
  //    [moment("01/04/2015", "DD/MM/YYYY").toDate(), 20.8],
  //    [moment("02/04/2015", "DD/MM/YYYY").toDate(), 19.2],
  //    ...
  // ]
  for(var i = 0; i < sensorsData.length; i++) {
    var sensorData = sensorsData[i];
    // the type temp, volt, illum, etc...
    var type = sensorData.channel.toLowerCase();

    if(dataMap[type]) {
      dataMap[type].data.push([moment(sensorData.dateTime, app.dateFormat).toDate(), sensorData.value]);
    }
  }

  // draw the charts
  for(var key in dataMap) {
    app.drawLineChart(dataMap[key].data, 'chart_' + key, dataMap[key].label, dataMap[key].color);
		var length = dataMap[key].data.length;

		// update the current value fields
		// only do it the first time we load the data, subsequently it's done by the channel message
		// app.handleChannelMessage
		if(!app.token) {
			if(dataMap[key].data.length > 0) {
				// the latest item is the current value
				sensorData = dataMap[key].data[length - 1];
				$('#value_' + key).animateNumber({ number: sensorData[1] });
			}
		}
  }

  if(initChannel) {
    app.token = data.token;

    // we have a channel token, now initialize it
    app.initChannel();
  }
}

/**
 * Draw a Google line chart
 * @param  {Array} rows - rows of data to draw
 * @param  {String} id - the id of the chart div
 * @param  {String} name - the name of the chart
 * @param  {String} color - the colour of the chart
 * @see https://developers.google.com/chart/interactive/docs/gallery/linechart?hl=en
 */
app.drawLineChart = function(rows, id, name, color) {
  var data = new google.visualization.DataTable();

  data.addColumn('datetime', 'Date');
  data.addColumn('number', name);

  data.addRows(rows);

  var options = {
    legend: {
			position: 'none'
		},
    colors: [color]
  };

  var chart = new google.visualization.LineChart(document.getElementById(id));
  chart.draw(data, options);
};

/**
 * initialise the channel
 * @see https://cloud.google.com/appengine/docs/java/channel/javascript
 */
app.initChannel = function() {
  setTimeout(function() {
		var channel = new goog.appengine.Channel(app.token);
		var handler = {
			'onopen': function() {
				console.log("socket opened");
		  },
			'onmessage': app.handleChannelMessage,
			'onerror': function() {
				toastr.error("Failed to load sensor data, please refresh the page");
			},
			'onclose': function() {
				console.log("Channel close");
				// if the channel token is expired, the channel will be closed.
				// in this case a reload should initialize a new token
				toastr.error("Failed to load sensor data, please refresh the page");
				/*setTimeout(function() {
					location.reload();
				}, 5000);*/
			}
		};
		var socket = channel.open(handler);

		socket.onmessage = app.handleChannelMessage;
  }, 500);
};

/**
 * Handle the Channel message
 * @param  {String} message - the channel message
 * @see https://cloud.google.com/appengine/docs/java/channel/javascript
 */
app.handleChannelMessage = function(message) {
	console.log(message);

  var data = JSON.parse(message.data);

  // the type temp, volt, illum, etc...
  var type = data.channel;

  // set the current value and animate it
  $('#value_' + type).animateNumber({ number: data.value });

  if(app.refreshChart) {
    app.loadData(false);
  }
};
