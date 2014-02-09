function drawLabel(group, cx, cy, width, height, text, fill) {
	var g = new Group().attr({
		x: cx,
		y: cy,
		cursor: 'pointer'
	}).addTo(group);

	new Rect(0, 0, width, height, 3).fill(fill).stroke('black', 2).addTo(g);

	// text must add last
	var t = new Text(text).addTo(g);
	var font_size = 16;
	var x_offset = (width - text.length * font_size)/2;
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

function drawArrow(group, cx, cy, len, height, fill) {
	var arrow = new Group().attr({
		x: cx,
		y: cy,
		cursor: 'pointer'
	}).addTo(group);
	
	var line = new Rect(0, 0, height, len);
	line.addTo(arrow).fill(fill);

	var hat = new Path().moveTo(-height, len).lineTo(height*2, len).lineTo(height/2, len+height).closePath();
	hat.addTo(arrow).fill(fill);
};

function draw() {
	var line_color = '#242424', in_out_color = '#ff6666', mid_color="yellow", name_color="#009966";
	
	var group = new Group().attr({
		x: 100,
		y: 250,
		cursor: 'pointer'
	}).addTo(stage);

	var start_x = -35, start_y = -237;

	var input_label_width = 80, input_label_height = 35;
	var mid_label_width = 130, mid_label_height = 35;

	var line_width = 2, arrow_width = 10;
	var line_length = 45;

	var horizontal_shift = 90;
	var vertical_shift = 40;

	var label_x = start_x;
	var label_y = start_y;

	for (var i=0; i<input.length; i++) {
		drawLabel(group, label_x, label_y, input_label_width, input_label_height, input[i], in_out_color);
		new Rect(label_x+35, label_y+input_label_height, line_width, line_length).fill(line_color).addTo(group);

		label_x = label_x + horizontal_shift;
	}


	new Rect(0, label_y+input_label_height+line_length, 
			(input.length-1)*horizontal_shift+2,
			2).fill(line_color).addTo(group);

	drawArrow(group, 
			(input.length-1)*horizontal_shift/2-arrow_width/2+line_width/2, 
			label_y+input_label_height+line_length+line_width, 
			middle.length*vertical_shift/2-arrow_width, 
			arrow_width, 
			line_color);

	drawLabel(group, 
			(input.length-1)*horizontal_shift/2-input_label_width/2+line_width/2,
			label_y+input_label_height+line_length+line_width+middle.length*vertical_shift/2, 
			input_label_width, 
			input_label_height, 
			processname, 
			name_color);

	drawArrow(group, 
			(input.length-1)*horizontal_shift/2-arrow_width/2+line_width/2, 
			label_y+input_label_height*2+line_length+line_width+middle.length*vertical_shift/2, 
			middle.length*vertical_shift/2-arrow_width, 
			arrow_width, 
			line_color);

	drawLabel(group, 
			(input.length-1)*horizontal_shift/2-input_label_width/2+line_width/2,
			label_y+input_label_height*2+line_length+line_width+middle.length*vertical_shift,
			input_label_width, 
			input_label_height, 
			output,
			in_out_color);

	label_y=start_y+input_label_height+line_length+line_width+vertical_shift;
	for (var i=0; i<middle.length; i++) {
		drawLabel(group, start_x+horizontal_shift, label_y, mid_label_width, mid_label_height, middle[i], mid_color);
		new Rect(start_x+horizontal_shift+mid_label_width, label_y+mid_label_height/2, line_length, line_width).fill(line_color).addTo(group);
		
		label_y = label_y + vertical_shift;
	}

	new Rect(start_x+horizontal_shift+mid_label_width+line_length, 
			start_y+input_label_height+line_length+line_width+vertical_shift+mid_label_height/2,
			line_width, 
			(middle.length-1)*vertical_shift+2).fill(line_color).addTo(group);

	var line_begin = start_x+horizontal_shift+mid_label_width+line_length+line_width;
	new Rect(line_begin,
			start_y+input_label_height+line_length+line_width+middle.length*vertical_shift/2+input_label_height/2,
			(input.length-1)*horizontal_shift/2-input_label_width/2+line_width/2-(line_begin),
			line_width).fill(line_color).addTo(group);
}

var processname = "轧钢";
var output = "蒸汽";
var input = new Array("焦粉焦丁", "焦炉煤气", "高炉煤气", "转炉煤气", "蒸汽", "净/原水", "环水", "软水", "氧气", "氮气", "氩气", "氢气", "压缩空气");
var middle = new Array("车轮轮箍", "二钢轧型材", "彩涂型材", "二钢轧棒材", "二钢轧线材", "一钢轧轧中板", "CSP连轧", "四钢轧冷轧板", "四钢轧热轧板", "冷轧交库量", "三钢轧轧高线线材", "三钢轧轧高线棒材", "三钢轧H型钢");

draw();