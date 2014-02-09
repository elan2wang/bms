/**
 * 箭头绘制
 * 
 * @param group
 * @param cx 箭头的起始点X轴值
 * @param cy 箭头的起始点Y轴值
 * @param len 箭头的箭柄长度
 * @param width 箭头的箭柄宽度
 * @param orientation 箭头的方向, 可选值: up, down, right
 * @param value 箭头上的数值
 * @param fill 箭头的填充色
 */
function drawArrow(group, cx, cy, len, width, orientation, value, fill) {
	var arrow = new Group().attr({
		x: cx,
		y: cy,
		cursor: 'pointer'
	}).addTo(group);

	var font_size = 14;

	if (orientation=='up') {
		var line = new Rect(0, width, width, len);
		line.addTo(arrow).fill(fill);

		var hat = new Path().moveTo(0-width, width).lineTo(width*2, width).lineTo(width/2, 0).closePath();
		hat.addTo(arrow).fill(fill);

		// text must add last
		var t = new Text(value).addTo(arrow);
		t.attr({
			x: -width*2,
			y: (len+width)/2 - font_size/2,
			fontSize: font_size,
			textFillColor: 'black',
			fontFamily: 'Arial',
		});
		arrow.text = t;

		return;
	}
	if (orientation=='down') {
		var line = new Rect(0, 0, width, len);
		line.addTo(arrow).fill(fill);

		var hat = new Path().moveTo(-width, len).lineTo(width*2, len).lineTo(width/2, len+width).closePath();
		hat.addTo(arrow).fill(fill);

		// text must add last
		var t = new Text(value).addTo(arrow);
		t.attr({
			x: -width*2,
			y: (len+width)/2 - font_size/2,
			fontSize: font_size,
			textFillColor: 'black',
			fontFamily: 'Arial',
		});
		arrow.text = t;
		return;
	}
	if (orientation=='right') {
		var line = new Rect(0, 0, len, width);
		line.addTo(arrow).fill(fill);

		var hat = new Path().moveTo(len, 0-width).lineTo(len, width*2).lineTo(len+width, width/2).closePath();
		hat.addTo(arrow).fill(fill);

		// text must add last
		var t = new Text(value).addTo(arrow);
		t.attr({
			x: len/2,
			y: 0 - (font_size - width)/2,
			fontSize: font_size,
			textFillColor: 'black',
			fontFamily: 'Arial',
		});
		arrow.text = t;
	}

};

/**
 * 文本框绘制
 * 
 * @param group
 * @param cx 文本框的起始点X轴值
 * @param cy 文本框的起始点Y轴值
 * @param text 文本框的显示文字
 * @param fill 文本框的填充色
 */
function drawLabel(group, cx, cy, text, fill) {
	var g = new Group().attr({
		x: cx,
		y: cy,
		cursor: 'pointer'
	}).addTo(group);

	new Rect(0, 0, 80, 35, 3).fill(fill).stroke('black', 2).addTo(g);

	// text must add last
	var t = new Text(text).addTo(g);
	var font_size = 16;
	var x_offset = (80 - text.length * font_size)/2;
	var y_offset = 11;
	t.attr({
		x: x_offset,
		y: y_offset,
		fontSize: font_size,
		textFillColor: 'black',
		fontFamily: 'Arial',
	});
	g.text = t;

};

/**
 * 圆柱绘制，圆柱为横放
 * 
 * @param group
 * @param x 圆柱尾部底面的中心X值
 * @param y 圆柱尾部底面的中心Y值
 * @param height 圆柱的高度(几何中的圆柱底面直径)
 * @param width 圆柱的宽度(几何中的圆柱高度)
 * @param fill 圆柱的填充色
 */
function drawCylinder(group, x, y, height, width, fill){
	var times = 10;
	var ry = height/2;
	var rx = ry/times;

	new Ellipse(x, y, rx, ry).addTo(group).fill(fill).stroke('black', 2);
	new Rect(x, y-ry-1, width, height+2).addTo(group).fill(fill);
	new Ellipse(x+width, y, rx, ry).addTo(group).fill(fill).stroke('black', 2);
};

stage.on('message:externalData', function(data) {
	var object = data.result;
	
    var group = new Group().attr({
    	x: 150,
    	y: 300,
    	cursor: 'pointer'
    }).addTo(stage);

    var fill1 = '#242424', fill2 = '#85dbc8';
    
    var maxHeight = 160, maxValue = object.in_item.value;
    	
    var width = 50, height = maxHeight,
    	x = 5, y = 0, in_name = object.in_item.name,
    	in_value = object.in_item.value;
    
    var label_start_x = -35, label_up_y = -237, label_down_y = 202;
    var arrow_start_x = 0, arrow_up_y = -200;
    var arrow_height = 200 - maxHeight/2 - 7;
    
    drawLabel(group, label_start_x, label_up_y, in_name, fill2);
    drawArrow(group, arrow_start_x, arrow_up_y, arrow_height, 5, 'down', in_value, fill2);
    for (var i=0;i<object.out_items.length;i++) {
    	drawCylinder(group, x, y, height, width, fill1);
    	x += width;
    	
    	var item = object.out_items[i];
    	arrow_start_y = height/2 + 3;
    	arrow_height = 200 - arrow_start_y - 5;
    	height = maxHeight * (in_value - item.value)/maxValue;
    	in_value = in_value - item.value;
    	label_start_x = label_start_x + width;
    	arrow_start_x = arrow_start_x + width;
    	
    	if (item.orientation == 'down') {
    		drawLabel(group, label_start_x, label_down_y, item.name, fill2);
    		drawArrow(group, arrow_start_x, arrow_start_y, arrow_height, 5, 'down', item.value, fill2);
    	} else if (item.orientation == 'up') {
    		drawLabel(group, label_start_x, label_up_y, item.name, fill2);
    		drawArrow(group, arrow_start_x, arrow_up_y, arrow_height, 5, 'up', item.value, fill2);
    	}
    }
    drawCylinder(group, x, y, height, width, fill1);
    x = x + width + 3;
    drawArrow(group, x, -2.5, 80, 5, 'right', object.left_item.value, fill2);
    x = x + 90;
    drawLabel(group, x, -18.5, object.left_item.name, fill2);
    
});

