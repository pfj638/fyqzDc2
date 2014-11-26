MM.Associate = function() {
	this._parent = null;
	this._from = null;
	this._to = null;

	this._fromAngle = null;
	this._toAngle = null;
	this._fromLenght = null;
	this._toLenght = null;
	
	this._color = null;
	this._id = MM.generateId();

	this._dom = {
		node: document.createElement("li"),
		toggle: document.createElement("div"),
		canvas: document.createElement("canvas")
	}
	this._dom.node.classList.add("item");
	this._dom.toggle.classList.add("toggle");

	this._dom.node.appendChild(this._dom.canvas);
	/* toggle+children are appended when children exist */

	this._dom.toggle.addEventListener("click", this);
}
MM.Associate.COLOR = "#999";

MM.Associate.prototype.update = function(doNotRecurse) {

}


MM.Associate.prototype.setFrom = function(from) {
	this._from = from;
	return this.update();
}

MM.Associate.prototype.getFrom = function() {
	return this._from;
}

MM.Associate.prototype.setFromAngle = function(fromAngle) {
	this._fromAngle = fromAngle;
	return this.update();
}

MM.Associate.prototype.getFromAngle = function() {
	return this._fromAngle;
}

MM.Associate.prototype.setFromLength = function(fromLength) {
	this._fromLength = fromLength;
	return this.update();
}

MM.Associate.prototype.getFromLength = function() {
	return this._fromLength;
}

MM.Associate.prototype.setTo = function(to) {
	this._to = to;
	return this.update();
}

MM.Associate.prototype.getTo = function() {
	return this._to;
}

MM.Associate.prototype.setToAngle = function(toAngle) {
	this._toAngle = toAngle;
	return this.update();
}

MM.Associate.prototype.getToAngle = function() {
	return this._toAngle;
}

MM.Associate.prototype.setToLength = function(toLength) {
	this._toLength = toLength;
	return this.update();
}

MM.Associate.prototype.getToLength = function() {
	return this._toLength;
}

MM.Associate.prototype.setColor = function(color) {
	this._color = color;
	return this.update();
}

MM.Associate.prototype.getColor = function() {
	return this._color || MM.Associate.COLOR ;
}

MM.Associate.prototype.getOwnColor = function() {
	return this._color;
}

MM.Associate.prototype.getDOM = function() {
	return this._dom;
}

MM.Associate.prototype.getMap = function() {
	var item = this._from;
	while (item) {
		if (item instanceof MM.Map) { return item; }
		item = item.getParent();
	}
	return null;
}

MM.Associate.prototype.insertAssociate = function(associate) {
	if(!associate.getFrom()){
		this.computerPosition(associate.getFrom(), associate.getFromAngle());
	}
}

MM.Associate.prototype.removeAssociate = function(associate) {
	
}

MM.Associate.prototype.startAssociate = function() {
	return this;
}

MM.Associate.prototype.stopAssociate = function() {

}

MM.Associate.prototype.handleEvent = function(e) {
	switch (e.type) {
		case "input":
			this.updateSubtree();
			this.getMap().ensureItemVisibility(this);
		break;

		case "keydown":
			if (e.keyCode == 9) { e.preventDefault(); } /* TAB has a special meaning in this app, do not use it to change focus */
		break;

		case "blur":
			MM.Command.Finish.execute();
		break;

		case "click":
			if (this._collapsed) { this.expand(); } else { this.collapse(); }
			MM.App.select(this);
		break;
	}
}
/*根据item的中心点和角度计算 item边框的坐标*/
MM.Associate.prototype.computerPosition = function(item, angle){
	var dom = item.getDom();
	var centerX = dom.node.offsetLeft + dom.node.offsetWidth/2;
	var centerY = dom.node.offsetTop + dom.node.offsetHeight/2;
	
	
}