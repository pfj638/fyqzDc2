/* My Mind web app: all source files combined. */
if (!Function.prototype.bind) {
	Function.prototype.bind = function(thisObj) {
		var fn = this;
		var args = Array.prototype.slice.call(arguments, 1);
		return function() {
			return fn.apply(thisObj, args.concat(Array.prototype.slice.call(arguments)));
		}
	}
};

var MM = {
	_subscribers: {},
	
	publish: function(message, publisher, data) {
		var subscribers = this._subscribers[message] || [];
		subscribers.forEach(function(subscriber) {
			subscriber.handleMessage(message, publisher, data);
		});
	},
	
	subscribe: function(message, subscriber) {
		if (!(message in this._subscribers)) {
			this._subscribers[message] = [];
		}
		this._subscribers[message].push(subscriber);
	},
	
	unsubscribe: function(message, subscriber) {
		var index = this._subscribers[message].indexOf(subscriber);
		this._subscribers[message].splice(index, 1);
	},
	
	generateId: function() {
		var str = "";
		for (var i=0;i<8;i++) {
			var code = Math.floor(Math.random()*26);
			str += String.fromCharCode("a".charCodeAt(0) + code);
		}
		return str;
	}
};
/*
 * Any copyright is dedicated to the Public Domain.
 * http://creativecommons.org/publicdomain/zero/1.0/
 */

/**
 * @class A promise - value to be resolved in the future. Implements the
 *        "Promises/A+" specification.
 */
var Promise = function() {
	this._state = 0; /* 0 = pending, 1 = fulfilled, 2 = rejected */
	this._value = null; /* fulfillment / rejection value */

	this._cb = {
		fulfilled: [],
		rejected: []
	}

	this._thenPromises = []; /* promises returned by then() */
}

/**
 * @param {function}
 *            onFulfilled To be called once this promise gets fulfilled
 * @param {function}
 *            onRejected To be called once this promise gets rejected
 * @returns {Promise}
 */
Promise.prototype.then = function(onFulfilled, onRejected) {
	this._cb.fulfilled.push(onFulfilled);
	this._cb.rejected.push(onRejected);

	var thenPromise = new Promise();

	this._thenPromises.push(thenPromise);

	if (this._state > 0) {
		setTimeout(this._processQueue.bind(this), 0);
	}

	/* 3.2.6. then must return a promise. */
	return thenPromise; 
}

/**
 * Fulfill this promise with a given value
 * 
 * @param {any}
 *            value
 */
Promise.prototype.fulfill = function(value) {
	if (this._state != 0) { return this; }

	this._state = 1;
	this._value = value;

	this._processQueue();

	return this;
}

/**
 * Reject this promise with a given value
 * 
 * @param {any}
 *            value
 */
Promise.prototype.reject = function(value) {
	if (this._state != 0) { return this; }

	this._state = 2;
	this._value = value;

	this._processQueue();

	return this;
}

/**
 * Pass this promise's resolved value to another promise
 * 
 * @param {Promise}
 *            promise
 */
Promise.prototype.chain = function(promise) {
	return this.then(promise.fulfill.bind(promise), promise.reject.bind(promise));
}

/**
 * @param {function}
 *            onRejected To be called once this promise gets rejected
 * @returns {Promise}
 */
Promise.prototype["catch"] = function(onRejected) {
	return this.then(null, onRejected);
}

Promise.prototype._processQueue = function() {
	while (this._thenPromises.length) {
		var onFulfilled = this._cb.fulfilled.shift();
		var onRejected = this._cb.rejected.shift();
		this._executeCallback(this._state == 1 ? onFulfilled : onRejected);
	}
}

Promise.prototype._executeCallback = function(cb) {
	var thenPromise = this._thenPromises.shift();

	if (typeof(cb) != "function") {
		if (this._state == 1) {
			/*
			 * 3.2.6.4. If onFulfilled is not a function and promise1 is
			 * fulfilled, promise2 must be fulfilled with the same value.
			 */
			thenPromise.fulfill(this._value);
		} else {
			/*
			 * 3.2.6.5. If onRejected is not a function and promise1 is
			 * rejected, promise2 must be rejected with the same reason.
			 */
			thenPromise.reject(this._value);
		}
		return;
	}

	try {
		var returned = cb(this._value);

		if (returned && typeof(returned.then) == "function") {
			/*
			 * 3.2.6.3. If either onFulfilled or onRejected returns a promise
			 * (call it returnedPromise), promise2 must assume the state of
			 * returnedPromise
			 */
			var fulfillThenPromise = function(value) { thenPromise.fulfill(value); }
			var rejectThenPromise = function(value) { thenPromise.reject(value); }
			returned.then(fulfillThenPromise, rejectThenPromise);
		} else {
			/*
			 * 3.2.6.1. If either onFulfilled or onRejected returns a value that
			 * is not a promise, promise2 must be fulfilled with that value.
			 */ 
			thenPromise.fulfill(returned);
		}

	} catch (e) {

		/*
		 * 3.2.6.2. If either onFulfilled or onRejected throws an exception,
		 * promise2 must be rejected with the thrown exception as the reason.
		 */
		thenPromise.reject(e); 

	}
}    
/**
 * Wait for all these promises to complete. One failed => this fails too.
 */
Promise.when = function(all) {
	var promise = new this();
	var counter = 0;
	var results = [];

	for (var i=0;i<all.length;i++) {
		counter++;
		all[i].then(function(index, result) {
			results[index] = result;
			counter--;
			if (!counter) { promise.fulfill(results); }
		}.bind(null, i), function(reason) {
			counter = 1/0;
			promise.reject(reason);
		});
	}

	return promise;
}

/**
 * Promise-based version of setTimeout
 */
Promise.setTimeout = function(ms) {
	var promise = new this();
	setTimeout(function() { promise.fulfill(); }, ms);
	return promise;
}

/**
 * Promise-based version of addEventListener
 */
Promise.event = function(element, event, capture) {
	var promise = new this();
	var cb = function(e) {
		element.removeEventListener(event, cb, capture);
		promise.fulfill(e);
	}
	element.addEventListener(event, cb, capture);
	return promise;
}

/**
 * Promise-based wait for CSS transition end
 */
Promise.transition = function(element) {
	if ("transition" in element.style) {
		return this.event(element, "transitionend", false);
	} else if ("webkitTransition" in element.style) {
		return this.event(element, "webkitTransitionEnd", false);
	} else {
		return new this().fulfill();
	}
}

/**
 * Promise-based version of XMLHttpRequest::send
 */
Promise.send = function(xhr, data) {
	var promise = new this();
	xhr.addEventListener("readystatechange", function(e) {
		if (e.target.readyState != 4) { return; }
		if (e.target.status == 200) {
			promise.fulfill(e.target);
		} else {
			promise.reject(e.target);
		}
	});
	xhr.send(data);
	return promise;
}

Promise.worker = function(url, message) {
	var promise = new this();
	var worker = new Worker(url);
	Promise.event(worker, "message").then(function(e) {
		promise.fulfill(e.data);
	});
	Promise.event(worker, "error").then(function(e) {
		promise.reject(e.message);
	});
	worker.postMessage(message);
	return promise;
}
/**
 * Prototype for all things categorizable: shapes, layouts, commands, formats,
 * backends...
 */
MM.Repo = {
	id: "", /* internal ID */
	label: "", /* human-readable label */
	getAll: function() {
		var all = [];
		for (var p in this) {
			var val = this[p];
			if (this.isPrototypeOf(val)) { all.push(val); }
		}
		return all;
	},
	getByProperty: function(property, value) {
		return this.getAll().filter(function(item) {
			return item[property] == value;
		})[0] || null;
	},
	getById: function(id) {
		return this.getByProperty("id", id);
	},
	buildOption: function() {
		var o = document.createElement("option");
		o.value = this.id;
		o.innerHTML = this.label;
		return o;
	}
}
MM.Item = function() {
	this._parent = null;
	this._children = [];
	this._collapsed = false;
	this._level = null;/*加入节点的层级 by xiaxuanyu*/
	this._layout = null;
	this._shape = null;
	this._autoShape = true;
	this._color = null;
	this._nodeColor = null;/* 加入节点颜色标识 by xiaxuanyu */
	this._fontColor = null;/* 加入字体颜色标识 by xiaxuanyu */
	this._value = null;
	this._comment = null;/* 加入备注标识 by xiaxuanyu */
	this._status = null;
	this._side = null; /* side preference */
	this._id = MM.generateId();
	this._oldText = "";
	this._tag = null;/* 加入标签标识 by xiaxuanyu */
	this._evaluate = [];/*加入评价指标*/

	this._computed = {
		value: 0,
		status: null
	}

	this._dom = {
		node: document.createElement("li"),
		content: document.createElement("div"),
		status: document.createElement("span"),
		value: document.createElement("span"),
		text: document.createElement("div"),
		children: document.createElement("ul"),
		toggle: document.createElement("div"),
		canvas: document.createElement("canvas"),
		tag: document.createElement("img"),/* 加入标签对应的html控件 by xiaxuanyu */
		comment: document.createElement("img"),/* 加入注解对应的html控件 by xiaxuanyu */
		evaluate: document.createElement("img")/* 加入评价指标对应的html控件 by xiaxuanyu */
	}
	this._dom.node.classList.add("item");
	this._dom.content.classList.add("content");
	this._dom.status.classList.add("status");
	this._dom.value.classList.add("value");
	this._dom.text.classList.add("text");
	this._dom.toggle.classList.add("toggle");
	this._dom.children.classList.add("children");
	this._dom.tag.classList.add("tag");/* 增加其对应的类 by xiaxuanyu */
	this._dom.comment.classList.add("comment");/* 增加其对应的类 by xiaxuanyu */
	this._dom.evaluate.classList.add("evaluate");/* 增加其对应的类 by xiaxuanyu */


	this._dom.content.insertBefore(this._dom.tag,this._dom.content.firstChild);/*
																				 * 增加标签显示
																				 * by
																				 * xiaxuanyu
																				 */
	this._dom.content.appendChild(this._dom.text); /*
													 * status+value are appended
													 * in layout
													 */
	this._dom.content.appendChild(this._dom.comment);/*
														 * 增加comment by
														 * xiaxuanyu
														 */
	this._dom.content.appendChild(this._dom.evaluate);/* 增加evaluate by xiaxuanyu*/
	this._dom.node.appendChild(this._dom.canvas);
	this._dom.node.appendChild(this._dom.content);
	/* toggle+children are appended when children exist */

	this._dom.toggle.addEventListener("click", this);
}

MM.Item.COLOR = "#999";
MM.Item.NODECOLOR =null;
MM.Item.FONTCOLOR ="#000";

    /*
	 * RE explanation:
	 * _________________________________________________________________________
	 * One of the three possible variants ____________________ scheme://x
	 * ___________________________ aa.bb.cc _______________________ aa.bb/
	 * ______ path, search __________________________ end with a non-forbidden
	 * char ______ end of word or end of string
	 */                                                                                                                           
MM.Item.RE = /\b(([a-z][\w-]+:\/\/\w)|(([\w-]+\.){2,}[a-z][\w-]+)|([\w-]+\.[a-z][\w-]+\/))[^\s]*([^\s,.;:?!<>\(\)\[\]'"])?($|\b)/i;

MM.Item.prototype.getItemById = function(id,item){
	var newItem = null;
	item._children.forEach(function(child){
		if(!newItem){
			if(child._id == id){
				newItem = child;
			}else{
				newItem = item.getItemById(id,child);
			}
		}
		
	});
	return newItem;
}

MM.Item.fromJSON = function(data) {
	return new this().fromJSON(data);
}

/**
 * Only when creating a new item. To merge existing items, use .mergeWith().
 */
MM.Item.prototype.fromJSON = function(data) {
	this.setText(data.text);
	if (data.id) { this._id = data.id; }
	if (data.side) { this._side = data.side; }
	if (data.level) { this._level = data.level; }
	if (data.color) { this._color = data.color; }
	if (data.nodeColor) { this._nodeColor = data.nodeColor; }/* by xiaxuanyu */
	if (data.fontColor) { this._fontColor = data.fontColor; }/* by xiaxuanyu */
	if (data.tag) { this._tag = data.tag; }/* by xiaxuanyu */
	if (data.comment) { this._comment = data.comment; }/* by xiaxuanyu */
	if (data.value) { this._value = data.value; }
	if (data.status) {
		this._status = data.status;
		if (this._status == "maybe") { this._status = "computed"; }
	}
	if (data.collapsed) { this.collapse(); }
	if (data.layout) { this._layout = MM.Layout.getById(data.layout); }
	if (data.shape) { this.setShape(MM.Shape.getById(data.shape)); }

	(data.children || []).forEach(function(child) {
		this.insertChild(MM.Item.fromJSON(child));
	}, this);

	if (data.evaluate) {
		var evaluates =[];
		for(var i=0;i<data.evaluate.length;i++){
			var evaluate = MM.Evaluate.fromJSON(data.evaluate[i]);
			evaluates.push(evaluate);
		} 
		this.setEvaluate(evaluates);
	}/* by xiaxuanyu */
	return this;
}

MM.Item.prototype.toJSON = function() {
	var data = {
		id: this._id,
		text: this.getText()
	}
	
	if (!this.isRoot() && this._parent) {data.parent = this._parent._id;}
	if (this._side) { data.side = this._side; }
	if (this._level) { data.level = this._level; }
	if (this._color) { data.color = this._color; }
	if (this._nodeColor) { data.nodeColor = this._nodeColor; }/* by xiaxuanyu */
	if (this._fontColor) { data.fontColor = this._fontColor; }/* by xiaxuanyu */
	if (this._tag) { data.tag = this._tag; }/* by xiaxuanyu */
	if (this._comment) { data.comment = this._comment; }/* by xiaxuanyu */
	if (this._value) { data.value = this._value; }
	if (this._status) { data.status = this._status; }
	if (this._layout) { data.layout = this._layout.id; }
	if (!this._autoShape) { data.shape = this._shape.id; }
	if (this._collapsed) { data.collapsed = 1; }
	if (this._children.length) {
		data.children = this._children.map(function(child) { return child.toJSON(); });
	}
	if (this._evaluate.length) { 
		var array = [];
		this._evaluate.forEach(function(evaluate){array.push(evaluate.toJSON());});
		data.evaluate = array;
	}/* by xiaxuanyu */

	return data;
}

MM.Item.prototype.clone = function() {
	var data = this.toJSON();

	var removeId = function(obj) {
		delete obj.id;
		obj.children && obj.children.forEach(removeId);
	}
	removeId(data);

	return this.constructor.fromJSON(data);
}

MM.Item.prototype.update = function(doNotRecurse) {
	MM.publish("item-change", this);
	var map = this.getMap();
	if (!map || !map.isVisible()) { return this; }

	if (this._autoShape) { /* check for changed auto-shape */
		var autoShape = this._getAutoShape();
		if (autoShape != this._shape) {
			if (this._shape) { this._shape.unset(this); }
			this._shape = autoShape;
			this._shape.set(this);
		}
	}
	
	this._updateStatus();
	this._updateValue();
	this._updateTag();/* 更新tag by xiaxuanyu */
	this._updateAssociate();/* 更新联系 by xiaxuanyu */
	this._updateComment();/* 更新comment by xiaxuanyu */
	this._updateEvaluate();/* 更新evaluate by xiaxuanyu */

	this._dom.node.classList[this._collapsed ? "add" : "remove"]("collapsed");
	this.getLayout().update(this);
	this.getShape().update(this);
	if (!this.isRoot() && !doNotRecurse) { this._parent.update(); }
	
	return this;
}

MM.Item.prototype.updateSubtree = function(isSubChild) {
	this._children.forEach(function(child) {
		child.updateSubtree(true);
	});
	return this.update(isSubChild);
}
/*add by xiaxuanyu*/
MM.Item.prototype.getOwnAssociate = function(){
	var map = this.getMap();
	var associates = map.getAssociate();
	var ownAssociates = [];
	for(var i=0;i<associates.length;i++){
		if(associates[i].getFrom()._id == this._id || associates[i].getTo()._id == this._id){
			ownAssociates.push(associates[i]);
		}
	}
	return ownAssociates;
}

MM.Item.prototype.setText = function(text) {
	this._dom.text.innerHTML = text.replace(/\n/g, "<br/>");
	this._findLinks(this._dom.text);
	return this.update();
}

MM.Item.prototype.getText = function() {
	return this._dom.text.innerHTML.replace(/<br\s*\/?>/g, "\n");
}

MM.Item.prototype.collapse = function() {
	if (this._collapsed) { return; }
	this._collapsed = true;
	return this.update();
}

MM.Item.prototype.expand = function() {
	if (!this._collapsed) { return; }
	this._collapsed = false;
	this.update();
	return this.updateSubtree();
}

MM.Item.prototype.isCollapsed = function() {
	return this._collapsed;
}

MM.Item.prototype.setValue = function(value) {
	this._value = value;
	return this.update();
}

MM.Item.prototype.getValue = function() {
	return this._value;
}

MM.Item.prototype.getComputedValue = function() {
	return this._computed.value;
}
/*add by xiaxuanyu start */
MM.Item.prototype.setLevel = function() {
	if(this.isRoot()){
		this._level = 1;
	}else{
		this._level = this._parent.getLevel() + 1;
	}
}

MM.Item.prototype.getLevel = function() {
	return this._level;
}
/*end*/

/* 加入备注的get和set 方法 by xiaxuanyu start */
MM.Item.prototype.setComment = function(comment) {
	this._comment = comment;
	return this.update();
}

MM.Item.prototype.getComment = function() {
	return this._comment;
}

MM.Item.prototype.getOwnComment = function() {
	return this._comment;
}
/* end */
/* 加入评价指标的get和set 方法 by xiaxuanyu start */
MM.Item.prototype.setEvaluate = function(evaluate) {
	this._evaluate = evaluate;
	return this.update();
}

MM.Item.prototype.getEvaluate = function() {
	return this._evaluate;
}

/* end */
MM.Item.prototype.setStatus = function(status) {
	this._status = status;
	return this.update();
}

MM.Item.prototype.getStatus = function() {
	return this._status;
}

MM.Item.prototype.getComputedStatus = function() {
	return this._computed.status;
}
/* 标签的get和set方法 by xiaxuanyu start */
MM.Item.prototype.setTag = function(tag) {
	this._tag = tag;
	return this.update();
}

MM.Item.prototype.getTag = function() {
	return this._tag;
}
/* end */

MM.Item.prototype.setSide = function(side) {
	this._side = side;
	return this;
}

MM.Item.prototype.getSide = function() {
	return this._side;
}

MM.Item.prototype.getParentSide = function() {
	return this._side || (this.isRoot() ? this._parent._root.getLayout().id : this._parent.getParentSide());
}

MM.Item.prototype.getChildren = function() {
	return this._children;
}

MM.Item.prototype.setColor = function(color) {
	this._color = color;
	return this.updateSubtree();
}
MM.Item.prototype.getColor = function() {
	return this._color ||(this.isRoot() ? MM.Item.COLOR : this._parent.getColor());
}
MM.Item.prototype.getOwnColor = function() {
	return this._color;
}

/* 加入节点和字体的get和set方法 by xiaxuanyu start */
MM.Item.prototype.setNodeColor = function(color) {
	this._nodeColor = color;
	return this.update();
}
MM.Item.prototype.setFontColor = function(color) {
	this._fontColor = color;
	return this.update();
}

MM.Item.prototype.getNodeColor = function() {
	return this._nodeColor || MM.Item.NODECOLOR ;
}
MM.Item.prototype.getFontColor = function() {
	return this._fontColor || MM.Item.FONTCOLOR ;
}


MM.Item.prototype.getOwnNodeColor = function() {
	return this._nodeColor;
}
MM.Item.prototype.getOwnFontColor = function() {
	return this._fontColor;
}
/* end */

MM.Item.prototype.getLayout = function() {
	return this._layout || this._parent.getLayout();
}

MM.Item.prototype.getOwnLayout = function() {
	return this._layout;
}

MM.Item.prototype.setLayout = function(layout) {
	this._layout = layout;
	return this.updateSubtree();	
}

MM.Item.prototype.getShape = function() {
	return this._shape;
}

MM.Item.prototype.getOwnShape = function() {
	return (this._autoShape ? null : this._shape);
}

MM.Item.prototype.setShape = function(shape) {
	if (this._shape) { this._shape.unset(this); }

	if (shape) {
		this._autoShape = false;
		this._shape = shape;
	} else {
		this._autoShape = true;
		this._shape = this._getAutoShape();
	}

	this._shape.set(this);
	return this.update();
}

MM.Item.prototype.getDOM = function() {
	return this._dom;
}

MM.Item.prototype.getMap = function() {
	var item = this._parent;
	while (item) {
		if (item instanceof MM.Map) { return item; }
		item = item.getParent();
	}
	return null;
}

MM.Item.prototype.getParent = function() {
	return this._parent;
}

MM.Item.prototype.getPosition = function() {
	var item = this;
	var position = new Object();
	var top = item._dom.node.offsetTop - item._dom.content.offsetTop;
	var left = item._dom.node.offsetLeft + item._dom.content.offsetLeft;
	var height = item._dom.node.offsetHeight;
	if(item.getLayout().id == "graph-bottom"){
		if(item._dom.toggle.style.top){
			top = top - item._dom.node.offsetHeight + parseInt(item._dom.toggle.style.top.substring(0,item._dom.toggle.style.top.length-2));
		}
	}
	if(item.getLayout().id == "graph-top"){
		if(item._dom.toggle.style.top){
			top = top + item._dom.content.offsetTop;
		}
	}
	if(item.getLayout().id == "tree-right" || item.getLayout().id == "tree-left"){
		if(item._dom.toggle.style.top){
			top = top - item._dom.node.offsetHeight + parseInt(item._dom.toggle.style.top.substring(0,item._dom.toggle.style.top.length-2));
		}
	}
	while(!item.isRoot() && null != item.getParent()){
		left += item.getParent()._dom.node.offsetLeft;
		top += item.getParent()._dom.node.offsetTop;
		item = item.getParent();
	}
	position.X = left;
	position.Y = top + height;
	return position;
}

MM.Item.prototype.isRoot = function() {
	return (this._parent instanceof MM.Map);
}

MM.Item.prototype.setParent = function(parent) {
	this._parent = parent;
	return this.updateSubtree();
}

MM.Item.prototype.insertChild = function(child, index) {
	/*
	 * Create or remove child as necessary. This must be done before computing
	 * the index (inserting own child)
	 */
	var newChild = false;
	if (!child) { 
		child = new MM.Item();
		newChild = true;
	} else if (child.getParent()) {
		child.getParent().removeOwnChild(child);
	}

	if (!this._children.length) {
		this._dom.node.appendChild(this._dom.toggle);
		this._dom.node.appendChild(this._dom.children);
	}

	if (arguments.length < 2) { index = this._children.length; }
	
	var next = null;
	if (index < this._children.length) { next = this._children[index].getDOM().node; }
	this._dom.children.insertBefore(child.getDOM().node, next);
	this._children.splice(index, 0, child);
	
	return child.setParent(this);
}
/* 增加联系 by xiaxuanyu start */
MM.Item.prototype.insertAssociate= function() {
	/*
	 * 创建一个折线作为联系，以自身为起点
	 */
	var newAssociate = null;
	if(this){
		if(!this.isRoot()){
			newAssociate = new MM.Associate();
			
			if(this.getParentSide() == "left"){
				newAssociate.setFromSide("left");
			}
			if(this.getParentSide() == "right"){
				newAssociate.setFromSide("right");
			}
			if(this.getLayout().id == "graph-top"){
				newAssociate.setFromSide("up");
			}
			if(this.getLayout().id == "graph-bottom" ){
				newAssociate.setFromSide("down");
			}
			if(this.getLayout().id == "graph-left" || this.getLayout().id == "tree-left"){
				newAssociate.setFromSide("left");
			}
			if(this.getLayout().id == "graph-right" || this.getLayout().id == "tree-right"){
				newAssociate.setFromSide("right");
			}
		}else{
			newAssociate.setFromSide("up");
		}
		
		newAssociate.setFromLength(25);// 默认值固定
		newAssociate.setFinish(true);
		newAssociate.setParent(this);
		newAssociate.setFrom(this);
	}
	if(newAssociate){
		return newAssociate.insertAssociate();
	}
}
/* end */
MM.Item.prototype.removeChild = function(child) {
	var index = this._children.indexOf(child);
	this._children.splice(index, 1);
	var node = child.getDOM().node;
	node.parentNode.removeChild(node);
	
	child.getOwnAssociate().forEach(function(associate){
		associate.removeAssociate();
	});
	child.setParent(null);
	
	if (!this._children.length) {
		this._dom.toggle.parentNode.removeChild(this._dom.toggle);
		this._dom.children.parentNode.removeChild(this._dom.children);
	}
	
	return this.update();
}

MM.Item.prototype.removeOwnChild = function(child) {
	var index = this._children.indexOf(child);
	this._children.splice(index, 1);
	var node = child.getDOM().node;
	node.parentNode.removeChild(node);
	
	child.setParent(null);
	
	if (!this._children.length) {
		this._dom.toggle.parentNode.removeChild(this._dom.toggle);
		this._dom.children.parentNode.removeChild(this._dom.children);
	}
	
	return this.update();
}

MM.Item.prototype.startEditing = function() {
	this._oldText = this.getText();
	this._dom.text.contentEditable = true;
	this._dom.text.focus();
	document.execCommand("styleWithCSS", null, false);

	this._dom.text.addEventListener("input", this);
	this._dom.text.addEventListener("keydown", this);
	this._dom.text.addEventListener("blur", this);
	return this;
}

MM.Item.prototype.stopEditing = function() {
	this._dom.text.removeEventListener("input", this);
	this._dom.text.removeEventListener("keydown", this);
	this._dom.text.removeEventListener("blur", this);

	this._dom.text.blur();
	this._dom.text.contentEditable = false;
	var result = this._dom.text.innerHTML;
	this._dom.text.innerHTML = this._oldText;
	this._oldText = "";
	return result;
}

MM.Item.prototype.handleEvent = function(e) {
	switch (e.type) {
		case "input":
			this.updateSubtree();
			this._updateAssociate(); /*跟新联系 by xiaxuanyu*/
			this.getMap().ensureItemVisibility(this);
		break;

		case "keydown":
			if (e.keyCode == 9) { e.preventDefault(); } /*
														 * TAB has a special
														 * meaning in this app,
														 * do not use it to
														 * change focus
														 */
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

MM.Item.prototype._getAutoShape = function() {
	var depth = 0;
	var node = this;
	while (!node.isRoot()) {
		depth++;
		node = node.getParent();
	}
	switch (depth) {
		case 0: return MM.Shape.Ellipse;
		case 1: return MM.Shape.Box;
		default: return MM.Shape.Underline;
	}
}

MM.Item.prototype._updateStatus = function() {
	this._dom.status.className = "status";
	this._dom.status.style.display = "";

	var status = this._status;
	if (this._status == "computed") {
		var childrenStatus = this._children.every(function(child) {
			return (child.getComputedStatus() !== false);
		});
		status = (childrenStatus ? "yes" : "no");
	}

	switch (status) {
		case "yes":
			this._dom.status.classList.add("yes");
			this._computed.status = true;
		break;

		case "no":
			this._dom.status.classList.add("no");
			this._computed.status = false;
		break;

		default:
			this._computed.status = null;
			this._dom.status.style.display = "none";
		break;
	}
}


MM.Item.prototype._updateValue = function() {
	this._dom.value.style.display = "";

	if (typeof(this._value) == "number") {
		this._computed.value = this._value;
		this._dom.value.innerHTML = this._value;
		return;
	}
	
	var childValues = this._children.map(function(child) {
		return child.getComputedValue();
	});
	
	var result = 0;
	switch (this._value) {
		case "sum":
			result = childValues.reduce(function(prev, cur) {
				return prev+cur;
			}, 0);
		break;
		
		case "avg":
			var sum = childValues.reduce(function(prev, cur) {
				return prev+cur;
			}, 0);
			result = (childValues.length ? sum/childValues.length : 0);
		break;
		
		case "max":
			result = Math.max.apply(Math, childValues);
		break;
		
		case "min":
			result = Math.min.apply(Math, childValues);
		break;
		
		default:
			this._computed.value = 0;
			this._dom.value.innerHTML = "";
			this._dom.value.style.display = "none";
			return;
		break;
	}
	
	this._computed.value = result;
	this._dom.value.innerHTML = (Math.round(result) == result ? result : result.toFixed(3));
}

/* 更新标签 by xiaxuanyu start */
MM.Item.prototype._updateTag = function(){
	this._dom.tag.style.display = "";
	var tag = this._tag;
	var tagSpan = document.querySelector("#tag");
	var tags = tagSpan.querySelectorAll("[data-tag]");
	if(null == tag){
		this._dom.tag.style.display="none";
		return;
	}
	for(var i=1;i<tags.length;i++){
		if(tag == tags[i].getAttribute("data-tag")){
			this._dom.tag.setAttribute("src",tags[i].getAttribute("src"));
			this._dom.tag.setAttribute("title",tags[i].getAttribute("title"));
		}
	}
	
	var height = this._dom.node.style.height;
	this._dom.tag.style.height = parseInt(height.substring(0,height.length-2))*0.6 + "px";

}
/* end */
/* 更新联系 by xiaxuanyu start */
MM.Item.prototype._updateAssociate = function(){
	var map = null;
	if (this instanceof MM.Map){
		map = this;
	}else{
		map = this.getMap();
	}
	if(map && 0 < map._associate.length){
		map._associate[0].update();
	}
	if(map){
		map.adjustAssociate();
	}
}
/* end */
/* 更新备注 by xiaxuanyu start */
MM.Item.prototype._updateComment = function(){
	this._dom.comment.style.display = "";
	var comment = this._comment;
	if(null == comment){
		this._dom.comment.style.display="none";
		return;
	}
	this._dom.comment.setAttribute("src","../icons/comment_icon.jpg");
	this._dom.comment.setAttribute("title","点击编辑");
	this._dom.comment.setAttribute("name","comment");
	var height = this._dom.node.style.height;
	this._dom.comment.style.height = parseInt(height.substring(0,height.length-2))*0.6 + "px";
}
/* end */
/* 更新评价指标 by xiaxuanyu start */
MM.Item.prototype._updateEvaluate = function(){
	this._dom.evaluate.style.display = "";
	var evaluate = this._evaluate;
	if(evaluate.length == 0){
		this._dom.evaluate.style.display="none";
		return;
	}
	this._dom.evaluate.setAttribute("src","../icons/evaluateIndex.png");
	this._dom.evaluate.setAttribute("title","点击编辑");
	this._dom.evaluate.setAttribute("name","evaluate");
	var height = this._dom.node.style.height;
	this._dom.evaluate.style.height = parseInt(height.substring(0,height.length-2))*0.6 + "px";
}
/* end */

MM.Item.prototype._findLinks = function(node) {

	var children = [].slice.call(node.childNodes);
	for (var i=0;i<children.length;i++) {
		var child = children[i];
		switch (child.nodeType) {
			case 1: /* element */
				if (child.nodeName.toLowerCase() == "a") { continue; }
				this._findLinks(child);
			break;
			
			case 3: /* text */
				var result = child.nodeValue.match(this.constructor.RE);
				if (result) {
					var before = child.nodeValue.substring(0, result.index);
					var after = child.nodeValue.substring(result.index + result[0].length);
					var link = document.createElement("a");
					link.innerHTML = link.href = result[0];
					
					if (before) {
						node.insertBefore(document.createTextNode(before), child);
					}

					node.insertBefore(link, child);
					
					if (after) {
						child.nodeValue = after;
						i--; /* re-try with the aftertext */
					} else {
						node.removeChild(child);
					}
				}
			break;
		}
	}
}
MM.Map = function(options) {
	var o = {
		root: "My Mind Map",
		layout: MM.Layout.Map
	}
	for (var p in options) { o[p] = options[p]; }
	this._root = null;
	this._visible = false;
	this._position = [0, 0];
	this._id = MM.generateId();
	this._isEnter = false;/* by xiaxuanyu */
	this._associate = [];/* by xiaxuanyu */

	this._setRoot(new MM.Item().setText(o.root).setLayout(o.layout));
	this._root.setLevel();
}
MM.Map.prototype.getItemById = function(id,item){
	var newItem = null;
	item._children.forEach(function(child){
		if(!newItem){
			if(child._id == id){
				newItem = child;
			}else{
				newItem = item.getItemById(id,child);
			}
		}
	});
	return newItem;
}
MM.Map.prototype.getById = function(id){

	if(this._id == id){
		return this;
	}
	
	return this.getItemById(id,this._root);
}

MM.Map.fromJSON = function(data) {
	return new this().fromJSON(data);
}

MM.Map.prototype.fromJSON = function(data) {
	if (data.id) { this._id = data.id; }
	this._setRoot(MM.Item.fromJSON(data.root));
	if(data.associate){
		for(var i=0;i<data.associate.length;i++){
			var as = MM.Associate.fromJSON(data.associate[i]);
			as._from = this.getById(as._from);
			as._to = this.getById(as._to);
			this.setAssociate(as);
		}	
	}
	return this;
}

MM.Map.prototype.toJSON = function() {
	var data = {
		root: this._root.toJSON(),
		id: this._id,
	};
	if(this._associate.length){
		var array = [];
		this._associate.forEach(function(associate){array.push(associate.toJSON());});
		data.associate = array;
	}
	return data;
}

MM.Map.prototype.isVisible = function() {
	return this._visible;
}

MM.Map.prototype.update = function() {
	this._root.updateSubtree();
	if(0 < this._associate.length){
		this._associate[0].update();
	}
	return this;
}

MM.Map.prototype.show = function(where) {
	var node = this._root.getDOM().node;
	where.appendChild(node);
	this._visible = true;
	this._root.updateSubtree();
	this.center();
	MM.App.select(this._root);
	return this;
}

MM.Map.prototype.hide = function() {
	var node = this._root.getDOM().node;
	node.parentNode.removeChild(node);
	this._visible = false;
	return this;
}

MM.Map.prototype.center = function() {
	var node = this._root.getDOM().node;
	var port = MM.App.portSize;
	var left = (port[0] - node.offsetWidth)/2;
	var top = (port[1] - node.offsetHeight)/2;
	
	this._moveTo(Math.round(left), Math.round(top));

	return this;
}

MM.Map.prototype.moveBy = function(dx, dy) {
	return this._moveTo(this._position[0]+dx, this._position[1]+dy);
}

MM.Map.prototype.getClosestItem = function(x, y) {
	var all = [];

	var scan = function(item) {
		var rect = item.getDOM().content.getBoundingClientRect();
		var dx = rect.left + rect.width/2 - x;
		var dy = rect.top + rect.height/2 - y;
		all.push({
			item: item,
			dx: dx,
			dy: dy
		});
		if (!item.isCollapsed()) { item.getChildren().forEach(scan); }
	}
	
	scan(this._root);
	
	all.sort(function(a, b) {
		var da = a.dx*a.dx + a.dy*a.dy;
		var db = b.dx*b.dx + b.dy*b.dy;
		return da-db;
	});
	
	return all[0];
}

MM.Map.prototype.getItemFor = function(node) {
	var port = this._root.getDOM().node.parentNode;
	var associatePort = port.parentNode;
	while (node && node != port && node != associatePort && !node.classList.contains("content")) {
		node = node.parentNode;
	}	
	if (node == port) { return null; }

	var scan = function(item, node) {
		if (item.getDOM().content == node) { return item; }
		var children = item.getChildren();
		for (var i=0;i<children.length;i++) {
			var result = scan(children[i], node);
			if (result) { return result; }
		}
		return null;
	}

	return scan(this._root, node);
}
/*判断target 的dom节点是否在另一个contain dom节点中*/
MM.Map.prototype.isIn = function(target,contain){
	if(target == contain) {return true;}
	var body = document.getElementsByTagName("body");
	while(target != body[0]){
		if(target.parentNode == contain){
			return true;
		}
		target = target.parentNode;
	}
	return false;
}
/*end*/
MM.Map.prototype.ensureItemVisibility = function(item) {
	var padding = 10;

	var node = item.getDOM().content;
	var itemRect = node.getBoundingClientRect();
	var root = this._root.getDOM().node;
	var parentRect = root.parentNode.getBoundingClientRect();

	var delta = [0, 0];

	var dx = parentRect.left-itemRect.left+padding;
	if (dx > 0) { delta[0] = dx; }
	var dx = parentRect.right-itemRect.right-padding;
	if (dx < 0) { delta[0] = dx; }

	var dy = parentRect.top-itemRect.top+padding;
	if (dy > 0) { delta[1] = dy; }
	var dy = parentRect.bottom-itemRect.bottom-padding;
	if (dy < 0) { delta[1] = dy; }

	if (delta[0] || delta[1]) {
		this.moveBy(delta[0], delta[1]);
	}
}
MM.Map.prototype.adjustAssociate = function(){
	var associates = this.getAssociate();
	var divs = document.querySelectorAll(".lineDiv");
	if(0 < associates.length){
		if(divs){
			for(var i=0;i<divs.length;i++){
				associatePort.removeChild(divs[i]);
			}
		}
		associates.forEach(function(associate){
			associate.adjustPosition();
			associate.drawConnector();
		});
	}
}
MM.Map.prototype.getParent = function() {
	return null;
}

MM.Map.prototype.getRoot = function() {
	return this._root;
}
/* by xiaxuanyu start */
MM.Map.prototype.getIsEnter = function() {
	return this._isEnter;
}
MM.Map.prototype.setIsEnter = function(flag) {
	this._isEnter = flag;
}

/* end */
MM.Map.prototype.getName = function() {
	var name = this._root.getText();
	return name.replace(/\n/g, " ").replace(/<.*?>/g, "").trim();
}

MM.Map.prototype.getId = function() {
	return this._id;
}

MM.Map.prototype.pick = function(item, direction) {
	var candidates = [];
	var currentRect = item.getDOM().content.getBoundingClientRect();

	this._getPickCandidates(currentRect, this._root, direction, candidates);
	if (!candidates.length) { return item; }

	candidates.sort(function(a, b) {
		return a.dist - b.dist;
	});

	return candidates[0].item;
}

MM.Map.prototype._getPickCandidates = function(currentRect, item, direction, candidates) {
	if (!item.isCollapsed()) {
		item.getChildren().forEach(function(child) {
			this._getPickCandidates(currentRect, child, direction, candidates);
		}, this);
	}

	var node = item.getDOM().content;
	var rect = node.getBoundingClientRect();

	if (direction == "left" || direction == "right") {
		var x1 = currentRect.left + currentRect.width/2;
		var x2 = rect.left + rect.width/2;
		if (direction == "left" && x2 > x1) { return; }
		if (direction == "right" && x2 < x1) { return; }

		var diff1 = currentRect.top - rect.bottom;
		var diff2 = rect.top - currentRect.bottom;
		var dist = Math.abs(x2-x1);
	} else {
		var y1 = currentRect.top + currentRect.height/2;
		var y2 = rect.top + rect.height/2;
		if (direction == "top" && y2 > y1) { return; }
		if (direction == "bottom" && y2 < y1) { return; }

		var diff1 = currentRect.left - rect.right;
		var diff2 = rect.left - currentRect.right;
		var dist = Math.abs(y2-y1);
	}

	var diff = Math.max(diff1, diff2);
	if (diff > 0) { return; }
	if (!dist || dist < diff) { return; }

	candidates.push({item:item, dist:dist});
}

MM.Map.prototype._moveTo = function(left, top) {
	this._position = [left, top];

	var node = this._root.getDOM().node;
	node.style.left = left + "px";
	node.style.top = top + "px";
	this.update();/* add by xiaxuanyu */
}

MM.Map.prototype._setRoot = function(item) {
	this._root = item;
	this._root.setParent(this);
}
/* 增加设置联系 by xiaxuanyu start */

MM.Map.prototype.setAssociate = function(associate){
	this._associate.push(associate);
	associate.setParent(this);
	this.update();
}
/*MM.Map.prototype.removeAssociate = function(associate){	
	associate.removeAssociate(associate);	
}*/
MM.Map.prototype.getAssociate = function(){
	return this._associate;
}
/* end */
MM.Keyboard = {};
MM.Keyboard.init = function() {
	window.addEventListener("keydown", this);
	window.addEventListener("keypress", this);
}

MM.Keyboard.handleEvent = function(e) {
	var node = document.activeElement;
	while (node && node != document) {
		if (node.classList.contains("ui")) { return; }
		node = node.parentNode;
	}
	
	var commands = MM.Command.getAll();
	for (var i=0;i<commands.length;i++) {
		var command = commands[i];
		if (!command.isValid()) { continue; }
		var keys = command.keys;
		for (var j=0;j<keys.length;j++) {
			if (this._keyOK(keys[j], e)) {
				e.preventDefault();
				command.execute(e);
				return;
			}
		}
	}
}

MM.Keyboard._keyOK = function(key, e) {
	if ("keyCode" in key && e.type != "keydown") { return false; }
	if ("charCode" in key && e.type != "keypress") { return false; }
	for (var p in key) {
		if (key[p] != e[p]) { return false; }
	}
	return true;
}
MM.Tip = {
	_node: null,

	handleEvent: function() {
		this._hide();
	},

	handleMessage: function() {
		this._hide();
	},

	init: function() {
		this._node = document.querySelector("#tip");
		this._node.addEventListener("click", this);

		MM.subscribe("command-child", this);
		MM.subscribe("command-sibling", this);
	},

	_hide: function() {
		MM.unsubscribe("command-child", this);
		MM.unsubscribe("command-sibling", this);

		this._node.removeEventListener("click", this);
		this._node.classList.add("hidden");
		this._node = null;
	}
}
MM.Action = function() {}
MM.Action.prototype.perform = function() {}
MM.Action.prototype.undo = function() {}

MM.Action.InsertNewItem = function(parent, index) {
	this._parent = parent;
	this._index = index;
	this._item = new MM.Item();
}
MM.Action.InsertNewItem.prototype = Object.create(MM.Action.prototype);
MM.Action.InsertNewItem.prototype.perform = function() {
	this._parent.expand(); /* FIXME remember? */
	this._item = this._parent.insertChild(this._item, this._index);
	this._item.setLevel();
	this._item._updateAssociate();/*add by xiaxuanyu*/
	MM.App.select(this._item);
}
MM.Action.InsertNewItem.prototype.undo = function() {
	this._parent.removeChild(this._item);
	this._item._updateAssociate();/*add by xiaxuanyu*/
	MM.App.select(this._parent);
}
/* 增加联系的action by xiaxuanyu start */
MM.Action.InsertNewAssociate = function(from) {
	this._from = from;
	this._associate = new MM.Associate();
}
MM.Action.InsertNewAssociate.prototype = Object.create(MM.Action.prototype);
MM.Action.InsertNewAssociate.prototype.perform = function() {
	this._associate = this._from.insertAssociate();
	MM.App.select(this._from);
}
MM.Action.InsertNewAssociate.prototype.undo = function() {
	this._associate.removeAssociate();
	MM.App.select(this._from);
}
/* end */
/* 增加评价指标的action by xiaxuanyu start */
MM.Action.Evaluate = function(item,evaluate) {
	this._item = item;
	this._evaluate = evaluate;
	this._oldEvaluate = item.getEvaluate();
}
MM.Action.Evaluate.prototype = Object.create(MM.Action.prototype);
MM.Action.Evaluate.prototype.perform = function() {
	this._item.setEvaluate(this._evaluate);
}
MM.Action.Evaluate.prototype.undo = function() {
	this._item.setEvaluate(this._oldEvaluate);
}
/* end */
MM.Action.AppendItem = function(parent, item) {
	this._parent = parent;
	this._item = item;
}
MM.Action.AppendItem.prototype = Object.create(MM.Action.prototype);
MM.Action.AppendItem.prototype.perform = function() {
	this._parent.insertChild(this._item);
	MM.App.select(this._item);
}
MM.Action.AppendItem.prototype.undo = function() {
	this._parent.removeChild(this._item);
	MM.App.select(this._parent);
}

MM.Action.RemoveItem = function(item) {
	this._item = item;
	this._parent = item.getParent();
	this._ownAssociates = item.getOwnAssociate();/*add by xiaxuanyu*/
	this._index = this._parent.getChildren().indexOf(this._item);
}
MM.Action.RemoveItem.prototype = Object.create(MM.Action.prototype);
MM.Action.RemoveItem.prototype.perform = function() {
	this._parent.removeChild(this._item);
	this._parent._updateAssociate();/*add by xiaxuanyu*/
	MM.App.select(this._parent);
}
MM.Action.RemoveItem.prototype.undo = function() {
	this._parent.insertChild(this._item, this._index);
	var map = this._item.getMap();
	for(var i=0;i<this._ownAssociates.length;i++){
		map.setAssociate(this._ownAssociates[i]);
	}
	this._parent._updateAssociate();/*add by xiaxuanyu*/
	MM.App.select(this._item);
}
/*by xiaxuanyu start*/
MM.Action.RemoveAssociate = function(associate) {
	this._associate = associate;
	this._parent = associate.getParent();
}
MM.Action.RemoveAssociate.prototype = Object.create(MM.Action.prototype);
MM.Action.RemoveAssociate.prototype.perform = function() {
	this._associate.removeAssociate();
	MM.App.select(this._parent._root);
}
MM.Action.RemoveAssociate.prototype.undo = function() {
	this._parent.setAssociate(this._associate);
	MM.App.select(this._parent._root);
}
/*end*/
MM.Action.MoveItem = function(item, newParent, newIndex, newSide) {
	this._item = item;
	this._newParent = newParent;
	this._newIndex = (arguments.length < 3 ? null : newIndex);
	this._newSide = newSide || "";
	this._oldParent = item.getParent();
	this._oldIndex = this._oldParent.getChildren().indexOf(item);
	this._oldSide = item.getSide();
}
MM.Action.MoveItem.prototype = Object.create(MM.Action.prototype);
MM.Action.MoveItem.prototype.perform = function() {
	this._item.setSide(this._newSide);
	if (this._newIndex === null) {
		this._newParent.insertChild(this._item);
	} else {
		this._newParent.insertChild(this._item, this._newIndex);
	}
	MM.App.select(this._item);
}
MM.Action.MoveItem.prototype.undo = function() {
	this._item.setSide(this._oldSide);
	this._oldParent.insertChild(this._item, this._oldIndex);
	MM.App.select(this._newParent);
}

MM.Action.Swap = function(item, diff) {
	this._item = item;
	this._parent = item.getParent();

	var children = this._parent.getChildren();
	var sibling = this._parent.getLayout().pickSibling(this._item, diff);
	
	this._sourceIndex = children.indexOf(this._item);
	this._targetIndex = children.indexOf(sibling);
}
MM.Action.Swap.prototype = Object.create(MM.Action.prototype);
MM.Action.Swap.prototype.perform = function() {

	this._parent.insertChild(this._item, this._targetIndex);
	this._item._updateAssociate();/*add by xiaxuanyu*/
}
MM.Action.Swap.prototype.undo = function() {
	this._parent.insertChild(this._item, this._sourceIndex);
	this._item._updateAssociate();/*add by xiaxuanyu*/
}

MM.Action.SetLayout = function(item, layout) {
	this._item = item;
	this._layout = layout;
	this._oldLayout = item.getOwnLayout();
}
MM.Action.SetLayout.prototype = Object.create(MM.Action.prototype);
MM.Action.SetLayout.prototype.perform = function() {
	this._item.setLayout(this._layout);
}
MM.Action.SetLayout.prototype.undo = function() {
	this._item.setLayout(this._oldLayout);
}

MM.Action.SetShape = function(item, shape) {
	this._item = item;
	this._shape = shape;
	this._oldShape = item.getOwnShape();
}
MM.Action.SetShape.prototype = Object.create(MM.Action.prototype);
MM.Action.SetShape.prototype.perform = function() {
	this._item.setShape(this._shape);
}
MM.Action.SetShape.prototype.undo = function() {
	this._item.setShape(this._oldShape);
}
/* 加入备注action by xiaxuanyu start */
MM.Action.SetComment = function(item, comment) {
	this._item = item;
	this._comment = comment;
	this._oldComment = item.getOwnComment();
}
MM.Action.SetComment.prototype = Object.create(MM.Action.prototype);
MM.Action.SetComment.prototype.perform = function() {
	this._item.setComment(this._comment);
}
MM.Action.SetComment.prototype.undo = function() {
	this._item.setComment(this._oldComment);
}
/* end */
/* 加入评分指标action by xiaxuanyu start */
MM.Action.SetEvaluate = function(item, evaluate) {
	this._item = item;
	this._evaluate = evaluate;
	this._oldEvaluate = item.getEvaluate();
}
MM.Action.SetEvaluate.prototype = Object.create(MM.Action.prototype);
MM.Action.SetEvaluate.prototype.perform = function() {
	this._item.setEvaluate(this._evaluate);
}
MM.Action.SetEvaluate.prototype.undo = function() {
	this._item.setEevaluate(this._oldEvaluate);
}
/* end */
MM.Action.SetColor = function(item, color) {
	this._item = item;
	this._color = color;
	this._oldColor = item.getOwnColor();
}
MM.Action.SetColor.prototype = Object.create(MM.Action.prototype);
MM.Action.SetColor.prototype.perform = function() {
	this._item.setColor(this._color);
}
MM.Action.SetColor.prototype.undo = function() {
	this._item.setColor(this._oldColor);
}
/* 加入修改节点和字体的颜色动作by xiaxuanyu start */
MM.Action.SetNodeColor = function(item, color) {
	this._item = item;
	this._nodeColor = color;
	this._oldNodeColor = item.getOwnNodeColor();
}
MM.Action.SetNodeColor.prototype = Object.create(MM.Action.prototype);
MM.Action.SetNodeColor.prototype.perform = function() {
	this._item.setNodeColor(this._nodeColor);
}
MM.Action.SetNodeColor.prototype.undo = function() {
	this._item.setNodeColor(this._oldNodeColor);
}

MM.Action.SetFontColor = function(item, color) {
	this._item = item;
	this._fontColor = color;
	this._oldFontColor = item.getOwnFontColor();
}
MM.Action.SetFontColor.prototype = Object.create(MM.Action.prototype);
MM.Action.SetFontColor.prototype.perform = function() {
	this._item.setFontColor(this._fontColor);
}
MM.Action.SetFontColor.prototype.undo = function() {
	this._item.setFontColor(this._oldFontColor);
}
/* end */
MM.Action.SetText = function(item, text) {
	this._item = item;
	this._text = text;
	this._oldText = item.getText();
	this._oldValue = item.getValue(); /* adjusting text can also modify value! */
}
MM.Action.SetText.prototype = Object.create(MM.Action.prototype);
MM.Action.SetText.prototype.perform = function() {
	this._item.setText(this._text);
	var numText = Number(this._text);
	if (numText == this._text) { this._item.setValue(numText); }
}
MM.Action.SetText.prototype.undo = function() {
	this._item.setText(this._oldText);
	this._item.setValue(this._oldValue);
}

MM.Action.SetValue = function(item, value) {
	this._item = item;
	this._value = value;
	this._oldValue = item.getValue();
}
MM.Action.SetValue.prototype = Object.create(MM.Action.prototype);
MM.Action.SetValue.prototype.perform = function() {
	this._item.setValue(this._value);
}
MM.Action.SetValue.prototype.undo = function() {
	this._item.setValue(this._oldValue);
}

MM.Action.SetStatus = function(item, status) {
	this._item = item;
	this._status = status;
	this._oldStatus = item.getStatus();
}
MM.Action.SetStatus.prototype = Object.create(MM.Action.prototype);
MM.Action.SetStatus.prototype.perform = function() {
	this._item.setStatus(this._status);
}
MM.Action.SetStatus.prototype.undo = function() {
	this._item.setStatus(this._oldStatus);
}
/* 增加标签action by xiaxuanyu start */
MM.Action.SetTag = function(item, tag) {
	this._item = item;
	this._tag = tag;
	this._oldTag = item.getTag();
}
MM.Action.SetTag.prototype = Object.create(MM.Action.prototype);
MM.Action.SetTag.prototype.perform = function() {
	this._item.setTag(this._tag);
}
MM.Action.SetTag.prototype.undo = function() {
	this._item.setTag(this._oldTag);
}
/* end */

MM.Action.SetSide = function(item, side) {
	this._item = item;
	this._side = side;
	this._oldSide = item.getSide();
}
MM.Action.SetSide.prototype = Object.create(MM.Action.prototype);
MM.Action.SetSide.prototype.perform = function() {
	this._item.setSide(this._side);
	this._item.getMap().update();
}
MM.Action.SetSide.prototype.undo = function() {
	this._item.setSide(this._oldSide);
	this._item.getMap().update();
}
MM.Clipboard = {
	_data: null,
	_mode: ""
};

MM.Clipboard.copy = function(sourceItem) {
	this._endCut();

	this._data = sourceItem.clone();
	this._mode = "copy";
}

MM.Clipboard.paste = function(targetItem) {
	if (!this._data) { return; }

	switch (this._mode) {
		case "cut":
			if (this._data == targetItem || this._data.getParent() == targetItem) { /*
																					 * abort
																					 * by
																					 * pasting
																					 * on
																					 * the
																					 * same
																					 * node
																					 * or
																					 * the
																					 * parent
																					 */
				this._endCut();
				return;
			}

			var item = targetItem;
			while (!item.isRoot()) {
				if (item == this._data) { return; } /*
													 * moving to a child =>
													 * forbidden
													 */
				item = item.getParent();
			}

			var action = new MM.Action.MoveItem(this._data, targetItem);
			MM.App.action(action);

			this._endCut();
		break;

		case "copy":
			var action = new MM.Action.AppendItem(targetItem, this._data.clone());
			MM.App.action(action);
		break;
	}

}

MM.Clipboard.cut = function(sourceItem) {
	this._endCut();

	this._data = sourceItem;
	this._mode = "cut";

	var node = this._data.getDOM().node;
	node.classList.add("cut");
}

MM.Clipboard._endCut = function() {
	if (this._mode != "cut") { return; }

	var node = this._data.getDOM().node;
	node.classList.remove("cut");

	this._data = null;
	this._mode = "";
}
MM.Menu = {
	_dom: {},
	_port: null,
	
	open: function(x, y) {
		this._dom.node.style.display = "";
		var w = this._dom.node.offsetWidth;
		var h = this._dom.node.offsetHeight;

		var left = x;
		var top = y;

		if (left > this._port.offsetWidth / 2) { left -= w; }
		if (top > this._port.offsetHeight / 2) { top -= h; }

		this._dom.node.style.left = left+"px";
		this._dom.node.style.top = top+"px";
	},
	
	close: function() {
		this._dom.node.style.display = "none";
	},
	
	handleEvent: function(e) {
		if (e.currentTarget != this._dom.node) {
			this.close();
			return;
		}
		
		e.stopPropagation(); /* no dragdrop, no blur of activeElement */
		e.preventDefault(); /* we do not want to focus the button */
		
		var command = e.target.getAttribute("data-command");
		if (!command) { return; }

		command = MM.Command[command];
		if (!command.isValid()) { return; }

		command.execute();
		this.close();
	},
	
	init: function(port) {
		this._port = port;
		this._dom.node = document.querySelector("#menu");
		var buttons = this._dom.node.querySelectorAll("[data-command]");
		[].slice.call(buttons).forEach(function(button) {
			button.innerHTML = MM.Command[button.getAttribute("data-command")].label;
		});
		
		this._port.addEventListener("mousedown", this);
		this._dom.node.addEventListener("mousedown", this);
		
		this.close();
	}
}

MM.Command = Object.create(MM.Repo, {
	keys: {value: []},
	editMode: {value: false},
	label: {value: ""}
});

MM.Command.isValid = function() {
	return (this.editMode === null || this.editMode == MM.App.editing);
}
MM.Command.execute = function() {}

MM.Command.Undo = Object.create(MM.Command, {
	label: {value: "Undo"},
	keys: {value: [{keyCode: "Z".charCodeAt(0), ctrlKey: true}]}
});
MM.Command.Undo.isValid = function() {
	return MM.Command.isValid.call(this) && !!MM.App.historyIndex;
}
MM.Command.Undo.execute = function() {
	MM.App.history[MM.App.historyIndex-1].undo();
	MM.App.historyIndex--;
}

MM.Command.Redo = Object.create(MM.Command, {
	label: {value: "Redo"},
	keys: {value: [{keyCode: "Y".charCodeAt(0), ctrlKey: true}]},
});
MM.Command.Redo.isValid = function() {
	return (MM.Command.isValid.call(this) && MM.App.historyIndex != MM.App.history.length);
}
MM.Command.Redo.execute = function() {
	MM.App.history[MM.App.historyIndex].perform();
	MM.App.historyIndex++;
}

MM.Command.InsertSibling = Object.create(MM.Command, {
	label: {value: "Insert a sibling"},
	keys: {value: [{keyCode: 13}]}
});
MM.Command.InsertSibling.execute = function() {
	var item = MM.App.current;
	if (item.isRoot()) {
		var action = new MM.Action.InsertNewItem(item, item.getChildren().length);
	} else {
		var parent = item.getParent();
		var index = parent.getChildren().indexOf(item);
		var action = new MM.Action.InsertNewItem(parent, index+1);
	}
	MM.App.action(action);

	MM.Command.Edit.execute();

	MM.publish("command-sibling");
		
}

MM.Command.InsertChild = Object.create(MM.Command, {
	label: {value: "Insert a child"},
	keys: {value: [
		{keyCode: 9, ctrlKey:false},
		{keyCode: 45}
	]}
});
MM.Command.InsertChild.execute = function() {
	var item = MM.App.current;
	var action = new MM.Action.InsertNewItem(item, item.getChildren().length);
	MM.App.action(action);	
	MM.Command.Edit.execute();

	MM.publish("command-child");
}
/* 增加线条联系命令 by xiaxuanyu start */
MM.Command.InsertAssociate = Object.create(MM.Command, {
	label: {value: "Insert associate"},
	keys: {value: [{keyCode: "A".charCodeAt(0), ctrlKey:false, altKey:true}]}
});
MM.Command.InsertAssociate.execute = function() {
	var item = MM.App.current;
	var action = new MM.Action.InsertNewAssociate(item);
	MM.App.action(action);	

}
/* end */
MM.Command.Delete = Object.create(MM.Command, {
	label: {value: "Delete an item"},
	keys: {value: [{keyCode: 46}]}
});
MM.Command.Delete.isValid = function() {
	return MM.Command.isValid.call(this) && !MM.App.current.isRoot();
}
MM.Command.Delete.execute = function() {
	var action = null;
	if(MM.App.associate){
		var map = MM.App.map;
		var associates = map.getAssociate();
		for(var i=0;i<associates.length;i++){
			if(associates[i].getSelect()){
				action = new MM.Action.RemoveAssociate(associates[i]);
			}
		}
	}else{
		action = new MM.Action.RemoveItem(MM.App.current);
	}
	MM.App.action(action);	
}

MM.Command.Swap = Object.create(MM.Command, {
	label: {value: "Swap sibling"},
	keys: {value: [
		{keyCode: 38, ctrlKey:true},
		{keyCode: 40, ctrlKey:true},
	]}
});
MM.Command.Swap.execute = function(e) {
	var current = MM.App.current;
	if (current.isRoot() || current.getParent().getChildren().length < 2) { return; }

	var diff = (e.keyCode == 38 ? -1 : 1);
	var action = new MM.Action.Swap(MM.App.current, diff);
	MM.App.action(action);	
}

MM.Command.Side = Object.create(MM.Command, {
	label: {value: "Change side"},
	keys: {value: [
		{keyCode: 37, ctrlKey:true},
		{keyCode: 39, ctrlKey:true},
	]}
});
MM.Command.Side.execute = function(e) {
	var current = MM.App.current;
	if (current.isRoot() || !current.getParent().isRoot()) { return; }

	var side = (e.keyCode == 37 ? "left" : "right");
	var action = new MM.Action.SetSide(MM.App.current, side);
	MM.App.action(action);
}

MM.Command.Save = Object.create(MM.Command, {
	label: {value: "Save map"},
	keys: {value: [{keyCode: "S".charCodeAt(0), ctrlKey:true, shiftKey:false}]}
});
MM.Command.Save.execute = function() {
	MM.App.io.quickSave();
}

MM.Command.SaveAs = Object.create(MM.Command, {
	label: {value: "Save as&hellip;"},
	keys: {value: [{keyCode: "S".charCodeAt(0), ctrlKey:true, shiftKey:true}]}
});
MM.Command.SaveAs.execute = function() {
	MM.App.io.show("save");
}

MM.Command.Load = Object.create(MM.Command, {
	label: {value: "Load map"},
	keys: {value: [{keyCode: "O".charCodeAt(0), ctrlKey:true}]}
});
MM.Command.Load.execute = function() {
	MM.App.io.show("load");
}

MM.Command.Center = Object.create(MM.Command, {
	label: {value: "Center map"},
	keys: {value: [{keyCode: 35}]}
});
MM.Command.Center.execute = function() {
	MM.App.map.center();
}

MM.Command.New = Object.create(MM.Command, {
	label: {value: "New map"},
	keys: {value: [{keyCode: "N".charCodeAt(0), ctrlKey:true}]}
});
MM.Command.New.execute = function() {
	if (!confirm("Throw away your current map and start a new one?")) { return; }
	var map = new MM.Map();
	MM.App.setMap(map);
	MM.publish("map-new", this);
}

MM.Command.ZoomIn = Object.create(MM.Command, {
	label: {value: "Zoom in"},
	keys: {value: [{charCode:"+".charCodeAt(0)}]}
});
MM.Command.ZoomIn.execute = function() {
	MM.App.adjustFontSize(1);
}

MM.Command.ZoomOut = Object.create(MM.Command, {
	label: {value: "Zoom out"},
	keys: {value: [{charCode:"-".charCodeAt(0)}]}
});
MM.Command.ZoomOut.execute = function() {
	MM.App.adjustFontSize(-1);
}

MM.Command.Help = Object.create(MM.Command, {
	label: {value: "Show/hide help"},
	keys: {value: [{charCode: "?".charCodeAt(0)}]}
});
MM.Command.Help.execute = function() {
	MM.App.help.toggle();
}

MM.Command.UI = Object.create(MM.Command, {
	label: {value: "Show/hide UI"},
	keys: {value: [{charCode: "*".charCodeAt(0)}]}
});
/*
 * MM.Command.UI.execute = function() { MM.App.ui.toggle(); }
 */

MM.Command.Pan = Object.create(MM.Command, {
	label: {value: "Pan the map"},
	keys: {value: [
		{keyCode: "W".charCodeAt(0), ctrlKey:false, altKey:false, metaKey:false},
		{keyCode: "A".charCodeAt(0), ctrlKey:false, altKey:false, metaKey:false},
		{keyCode: "S".charCodeAt(0), ctrlKey:false, altKey:false, metaKey:false},
		{keyCode: "D".charCodeAt(0), ctrlKey:false, altKey:false, metaKey:false}
	]},
	chars: {value: []}
});
MM.Command.Pan.execute = function(e) {
	var ch = String.fromCharCode(e.keyCode);
	var index = this.chars.indexOf(ch);
	if (index > -1) { return; }

	if (!this.chars.length) {
		window.addEventListener("keyup", this);
		this.interval = setInterval(this._step.bind(this), 50);
	}

	this.chars.push(ch);
	this._step();
}

MM.Command.Pan._step = function() {
	var dirs = {
		"W": [0, 1],
		"A": [1, 0],
		"S": [0, -1],
		"D": [-1, 0]
	}
	var offset = [0, 0];

	this.chars.forEach(function(ch) {
		offset[0] += dirs[ch][0];
		offset[1] += dirs[ch][1];
	});

	MM.App.map.moveBy(15*offset[0], 15*offset[1]);
}

MM.Command.Pan.handleEvent = function(e) {
	var ch = String.fromCharCode(e.keyCode);
	var index = this.chars.indexOf(ch);
	if (index > -1) {
		this.chars.splice(index, 1);
		if (!this.chars.length) {
			window.removeEventListener("keyup", this);
			clearInterval(this.interval);
		}
	}
}

MM.Command.Copy = Object.create(MM.Command, {
	label: {value: "Copy"},
	keys: {value: [{keyCode: "C".charCodeAt(0), ctrlKey:true}]}
});
MM.Command.Copy.execute = function() {
	MM.Clipboard.copy(MM.App.current);
}

MM.Command.Cut = Object.create(MM.Command, {
	label: {value: "Cut"},
	keys: {value: [{keyCode: "X".charCodeAt(0), ctrlKey:true}]}
});
MM.Command.Cut.execute = function() {
	MM.Clipboard.cut(MM.App.current);
}

MM.Command.Paste = Object.create(MM.Command, {
	label: {value: "Paste"},
	keys: {value: [{keyCode: "V".charCodeAt(0), ctrlKey:true}]}
});
MM.Command.Paste.execute = function() {
	MM.Clipboard.paste(MM.App.current);
}

MM.Command.Fold = Object.create(MM.Command, {
	label: {value: "Fold/Unfold"},
	keys: {value: [{charCode: "f".charCodeAt(0), ctrlKey:false}]}
});
MM.Command.Fold.execute = function() {
	var item = MM.App.current;
	if (item.isCollapsed()) { item.expand(); } else { item.collapse(); }
	MM.App.map.ensureItemVisibility(item);
}
MM.Command.Edit = Object.create(MM.Command, {
	label: {value: "Edit item"},
	keys: {value: [
		{keyCode: 32},
		{keyCode: 113}
	]}
});
MM.Command.Edit.execute = function() {
	MM.App.current.startEditing();
	MM.App.editing = true;
}

MM.Command.Finish = Object.create(MM.Command, {
	keys: {value: [{keyCode: 13, altKey:false, ctrlKey:false, shiftKey:false}]},
	editMode: {value: true}
});
MM.Command.Finish.execute = function() {
	if(!MM.App.map.getIsEnter()){/* 加入完成条件 by xiaxuanyu */
		MM.App.editing = false;
		var text = MM.App.current.stopEditing();
		if (text) {
			var action = new MM.Action.SetText(MM.App.current, text);
		} else {
			var action = new MM.Action.RemoveItem(MM.App.current);
		}
		MM.App.action(action);
	}
	
}

MM.Command.Newline = Object.create(MM.Command, {
	label: {value: "Line break"},
	keys: {value: [
		{keyCode: 13, shiftKey:true},
		{keyCode: 13, ctrlKey:true}
	]},
	editMode: {value: true}
});
MM.Command.Newline.execute = function() {
	var range = getSelection().getRangeAt(0);
	var br = document.createElement("br");
	range.insertNode(br);
	range.setStartAfter(br);
	MM.App.current.updateSubtree();
}

MM.Command.Cancel = Object.create(MM.Command, {
	editMode: {value: true},
	keys: {value: [{keyCode: 27}]}
});
MM.Command.Cancel.isValid = function() {
	return MM.Command.isValid.call(this) || MM.App.associate;
}
MM.Command.Cancel.execute = function() {
	
	
	if(MM.App.associate){
		var map = MM.App.map;
		var associates = map.getAssociate();
		for(var i=0;i<associates.length;i++){
			if(!associates[i].getTo()){
				action = new MM.Action.RemoveAssociate(associates[i]);
			}
		}
	}else{
		MM.App.editing = false;
		MM.App.current.stopEditing();
		var oldText = MM.App.current.getText();
		var action = null;
		if (!oldText) { /* newly added node */
			action = new MM.Action.RemoveItem(MM.App.current);
		}
	}
	if(action){
		MM.App.action(action);
	}
}

MM.Command.Style = Object.create(MM.Command, {
	editMode: {value: null},
	command: {value: ""}
});

MM.Command.Style.execute = function() {
	if(!MM.App.map.getIsEnter()){/* 加入控制条件 by xiaxuanyu */
		if (MM.App.editing) {
			document.execCommand(this.command, null, null);
		} else {
			MM.Command.Edit.execute();
			var selection = getSelection();
			var range = selection.getRangeAt(0);
			range.selectNodeContents(MM.App.current.getDOM().text);
			selection.removeAllRanges();
			selection.addRange(range);
			this.execute();
			MM.Command.Finish.execute();
		}
	}
	
}

MM.Command.Bold = Object.create(MM.Command.Style, {
	command: {value: "bold"},
	label: {value: "Bold"},
	keys: {value: [{keyCode: "B".charCodeAt(0), ctrlKey:true}]}
});

MM.Command.Underline = Object.create(MM.Command.Style, {
	command: {value: "underline"},
	label: {value: "Underline"},
	keys: {value: [{keyCode: "U".charCodeAt(0), ctrlKey:true}]}
});

MM.Command.Italic = Object.create(MM.Command.Style, {
	command: {value: "italic"},
	label: {value: "Italic"},
	keys: {value: [{keyCode: "I".charCodeAt(0), ctrlKey:true}]}
});

MM.Command.Strikethrough = Object.create(MM.Command.Style, {
	command: {value: "strikeThrough"},
	label: {value: "Strike-through"},
	keys: {value: [{keyCode: "S".charCodeAt(0), ctrlKey:true}]}
});

MM.Command.Value = Object.create(MM.Command, {
	label: {value: "Set value"},
	keys: {value: [{charCode: "v".charCodeAt(0), ctrlKey:false}]}
});
MM.Command.Value.execute = function() {
	var item = MM.App.current;
	var oldValue = item.getValue();
	var newValue = prompt("Set item value", oldValue);
	if (newValue == null) { return; }

	if (!newValue.length) { newValue = null; }

	var numValue = parseFloat(newValue);
	var action = new MM.Action.SetValue(item, isNaN(numValue) ? newValue : numValue);
	MM.App.action(action);
}

/* 添加备注命令 by xiaxuanyu start */
MM.Command.Comment = Object.create(MM.Command, {
	label: {value: "Set comment"},
	keys: {value: [{charCode: "c".charCodeAt(0), ctrlKey:false, altKey:false}]}
});
MM.Command.Comment.execute = function() {
	var item = MM.App.current;
	MM.App.editing=true;
	MM.App.map.setIsEnter(true);
	var oldComment = item.getComment();
	var comment = document.getElementById("commentBox");
	document.getElementById("commentBox").value = oldComment;
	var top = item._dom.node.offsetTop - item._dom.content.offsetTop;
	var left = item._dom.node.offsetLeft + item._dom.content.offsetLeft;
	var height = item._dom.node.offsetHeight;
	if(item.getLayout().id == "graph-bottom"){
		if(item._dom.toggle.style.top){
			top = top - item._dom.node.offsetHeight + parseInt(item._dom.toggle.style.top.substring(0,item._dom.toggle.style.top.length-2));
		}
	}
	if(item.getLayout().id == "graph-top"){
		if(item._dom.toggle.style.top){
			top = top + item._dom.content.offsetTop;
		}
	}
	if(item.getLayout().id == "tree-right" || item.getLayout().id == "tree-left"){
		if(item._dom.toggle.style.top){
			top = top - item._dom.node.offsetHeight + parseInt(item._dom.toggle.style.top.substring(0,item._dom.toggle.style.top.length-2));
		}
	}
	while(!item.isRoot() && null != item.getParent()){
		left += item.getParent()._dom.node.offsetLeft;
		top += item.getParent()._dom.node.offsetTop;
		item = item.getParent();
	}

	
	comment.style.left = left+"px";
	comment.style.top = top + height + "px";
	comment.style.display="";
	comment.focus();
	if(document.selection){
		var sel = comment.createTextRange();
		sel.moveStart('character',comment.value.length);
		sel.collapse();
		sel.select();
	}else if(typeof comment.selectionStart === "number" && typeof comment.selectionEnd === "number"){
		comment.selectionStart = comment.selectionEnd = comment.value.length;
	}
	comment.addEventListener("blur", stopEdit,false);
	comment.addEventListener("keydown", keyInput,false);

}
var stopEdit=function(){
	var item = MM.App.current;
	MM.App.editing=false;
	MM.App.map.setIsEnter(false);
	var comment = document.getElementById("commentBox");
	var newComment = comment.value;
	comment.removeEventListener("blur",stopEdit,false);
	comment.removeEventListener("keydown",keyInput,false);
	comment.style.display="none";
	if (newComment == null) { return; }

	if (!newComment.length) { newComment = null; }
	var action = new MM.Action.SetComment(item, newComment);
	MM.App.action(action);	
}

var keyInput = function(event){
	MM.App.editing=true;
	MM.App.map.setIsEnter(true);
	if (event.keyCode == 13) {
		if(document.selection){
			var sel = document.selection.createRange();
			sel.text = "\n";
		}else if (typeof this.selectionStart === "number" && typeof this.selectionEnd === "number"){
			var startPos = this.selectionStart;
			var endPos = this.selectionEnd;
			var text = this.value;
			this.value = text.substring(0,startPos) + "\n" +text.substring(endPos,text.length);
		}
	}
	if (event.keyCode == 27) {this.blur();}
	
}

/* end */
/* 添加评价指标命令 by xiaxuanyu start */
MM.Command.Evaluate = Object.create(MM.Command, {
	label: {value: "Set evaluate"},
	keys: {value: [{charCode: "p".charCodeAt(0), ctrlKey:false, altKey:false}]}
});
MM.Command.Evaluate.execute = function() {
	var item = MM.App.current;
	MM.App.editing=true;
	MM.App.map.setIsEnter(true);
	var evaluateDiv = document.getElementById("evaluateIndex");
	var evaluateTable = document.getElementById("evaluateTable");
	evaluateDiv.style.display="";
	var evaluates=item.getEvaluate();
	for(var i=1;i<evaluateTable.rows.length;){
		evaluateTable.deleteRow(i);
	}
	addRow(evaluates);
	evaluateDiv.focus();
	evaluateDiv.addEventListener("keydown", evaluateInput,false);
}
function evaluateEdit(){
	var item = MM.App.current;
	var evaluateDiv = document.getElementById("evaluateIndex");
	MM.App.editing=false;
	MM.App.map.setIsEnter(false);
	evaluateDiv.style.display="none";
	var table = document.querySelector("#evaluateTable");
	
	if(!item.isRoot()){
		var evaluates = [];
		for(var i=1;i<table.rows.length;i++){
			var evaluate = new MM.Evaluate();
			var name=document.getElementsByName('mangeIndex['+i+'].name');
			var unit=document.getElementsByName('mangeIndex['+i+'].unit');
			var type=document.getElementsByName('mangeIndex['+i+'].type');
			if(name.length && name[0].value!=""){
				evaluate.setName(name[0].value);
			}
			if(unit.length && unit[0].value!=""){
				evaluate.setUnit(unit[0].value);
			}
			if(type.length && type[0].value!=""){
				evaluate.setType(type[0].value);
			}
			evaluates.push(evaluate);
		}
		var action = new MM.Action.Evaluate(item, evaluates);
		MM.App.action(action);
	}
	evaluateDiv.removeEventListener("keydown", evaluateInput,false);
	
}
function evaluateInput(event){
	MM.App.editing=true;
	MM.App.map.setIsEnter(true);
	var evaluateDiv = document.getElementById("evaluateIndex");
	if (event.keyCode == 27) {
		evaluateEdit();
	}
	if (event.keyCode == 13 ) {
		return;
	}
	
	
}
/*end*/
MM.Command.Yes = Object.create(MM.Command, {
	label: {value: "Yes"},
	keys: {value: [{charCode: "y".charCodeAt(0), ctrlKey:false}]}
});
MM.Command.Yes.execute = function() {
	var item = MM.App.current;
	var status = (item.getStatus() == "yes" ? null : "yes");
	var action = new MM.Action.SetStatus(item, status);
	MM.App.action(action);
}

MM.Command.No = Object.create(MM.Command, {
	label: {value: "No"},
	keys: {value: [{charCode: "n".charCodeAt(0), ctrlKey:false}]}
});
MM.Command.No.execute = function() {
	var item = MM.App.current;
	var status = (item.getStatus() == "no" ? null : "no");
	var action = new MM.Action.SetStatus(item, status);
	MM.App.action(action);
}

MM.Command.Computed = Object.create(MM.Command, {
	label: {value: "Computed"},
	keys: {value: [{charCode: "c".charCodeAt(0), ctrlKey:false, altKey:true}]}
});
MM.Command.Computed.execute = function() {
	var item = MM.App.current;
	var status = (item.getStatus() == "computed" ? null : "computed");
	var action = new MM.Action.SetStatus(item, status);
	MM.App.action(action);
}
MM.Command.Select = Object.create(MM.Command, {
	label: {value: "Move selection"},
	keys: {value: [
		{keyCode: 38, ctrlKey:false},
		{keyCode: 37, ctrlKey:false},
		{keyCode: 40, ctrlKey:false},
		{keyCode: 39, ctrlKey:false}
	]} 
});
MM.Command.Select.execute = function(e) {
	var dirs = {
		37: "left",
		38: "top",
		39: "right",
		40: "bottom"
	}
	var dir = dirs[e.keyCode];

	var layout = MM.App.current.getLayout();
	var item = /* MM.App.map */layout.pick(MM.App.current, dir);
	MM.App.select(item);
}

MM.Command.SelectRoot = Object.create(MM.Command, {
	label: {value: "Select root"},
	keys: {value: [{keyCode: 36}]}
});
MM.Command.SelectRoot.execute = function() {
	var item = MM.App.current;
	while (!item.isRoot()) { item = item.getParent(); }
	MM.App.select(item);
}

MM.Command.SelectParent = Object.create(MM.Command, {
	label: {value: "Select parent"},
	keys: {value: [{keyCode: 8}]}
});
MM.Command.SelectParent.execute = function() {
	if (MM.App.current.isRoot()) { return; }
	MM.App.select(MM.App.current.getParent());
}

MM.Layout = Object.create(MM.Repo, {
	ALL: {value: []},
	SPACING_RANK: {value: 4},
	SPACING_CHILD: {value: 4},
});

MM.Layout.getAll = function() {
	return this.ALL;
}

/**
 * Re-draw an item and its children
 */
MM.Layout.update = function(item) {
	return this;
}

/**
 * @param {MM.Item}
 *            child Child node (its parent uses this layout)
 */
MM.Layout.getChildDirection = function(child) {
	return "";
}

MM.Layout.pick = function(item, dir) {
	var opposite = {
		left: "right",
		right: "left",
		top: "bottom",
		bottom: "top"
	}
	
	/* direction for a child */
	if (!item.isCollapsed()) {
		var children = item.getChildren();
		for (var i=0;i<children.length;i++) {
			var child = children[i];
			if (this.getChildDirection(child) == dir) { return child; }
		}
	}

	if (item.isRoot()) { return item; }
	
	var parentLayout = item.getParent().getLayout();
	var thisChildDirection = parentLayout.getChildDirection(item);
	if (thisChildDirection == dir) {
		return item;
	} else if (thisChildDirection == opposite[dir]) {
		return item.getParent();
	} else {
		return parentLayout.pickSibling(item, (dir == "left" || dir == "top" ? -1 : +1));
	}
}

MM.Layout.pickSibling = function(item, dir) {
	if (item.isRoot()) { return item; }

	var children = item.getParent().getChildren();
	var index = children.indexOf(item);
	index += dir;
	index = (index+children.length) % children.length;
	return children[index];
}

/**
 * Adjust canvas size and position
 */
MM.Layout._anchorCanvas = function(item) {
	var dom = item.getDOM();
	dom.canvas.width = dom.node.offsetWidth;
	dom.canvas.height = dom.node.offsetHeight;
}

MM.Layout._anchorToggle = function(item, x, y, side) {
	var node = item.getDOM().toggle;
	var w = node.offsetWidth;
	var h = node.offsetHeight;
	var l = x;
	var t = y;

	switch (side) {
		case "left":
			t -= h/2;
			l -= w;
		break;

		case "right":
			t -= h/2;
		break;
		
		case "top":
			l -= w/2;
			t -= h;
		break;

		case "bottom":
			l -= w/2;
		break;
	}
	
	node.style.left = Math.round(l) + "px";
	node.style.top = Math.round(t) + "px";
}

MM.Layout._getChildAnchor = function(item, side) {
	var dom = item.getDOM();
	if (side == "left" || side == "right") {
		var pos = dom.node.offsetLeft + dom.content.offsetLeft;
		if (side == "left") { pos += dom.content.offsetWidth; }
	} else {
		var pos = dom.node.offsetTop + dom.content.offsetTop;
		if (side == "top") { pos += dom.content.offsetHeight; }
	}
	return pos;
}

MM.Layout._computeChildrenBBox = function(children, childIndex) {
	var bbox = [0, 0];
	var rankIndex = (childIndex+1) % 2;

	children.forEach(function(child, index) {
		var node = child.getDOM().node;
		var childSize = [node.offsetWidth, node.offsetHeight];

		bbox[rankIndex] = Math.max(bbox[rankIndex], childSize[rankIndex]); /*
																			 * adjust
																			 * cardinal
																			 * size
																			 */
		bbox[childIndex] += childSize[childIndex]; /* adjust orthogonal size */
	}, this);

	if (children.length > 1) { bbox[childIndex] += this.SPACING_CHILD * (children.length-1); } /*
																								 * child
																								 * separation
																								 */

	return bbox;
}

MM.Layout._alignItem = function(item, side) {
	var dom = item.getDOM();

	switch (side) {
		case "left":
			dom.content.appendChild(dom.value);
			dom.content.appendChild(dom.status);
			// dom.content.appendChild(dom.tag);/*增加tag by xiaxuanyu*/
			// dom.content.appendChild(dom.comment);/*增加comment by xiaxuanyu*/
		break;
		case "right":
			dom.content.insertBefore(dom.value, dom.content.firstChild);
			dom.content.insertBefore(dom.status, dom.content.firstChild);
			// dom.content.insertBefore(dom.tag, dom.content.firstChild);/*增加tag
			// by xiaxuanyu*/
			// dom.content.insertBefore(dom.comment,
			// dom.content.firstChild);/*增加comment by xiaxuanyu*/
		break;
	}
}
MM.Layout.Graph = Object.create(MM.Layout, {
	SPACING_RANK: {value: 16},
	childDirection: {value: ""}
});

MM.Layout.Graph.getChildDirection = function(child) {
	return this.childDirection;
}

MM.Layout.Graph.create = function(direction, id, label) {
	var layout = Object.create(this, {
		childDirection: {value:direction},
		id: {value:id},
		label: {value:label}
	});
	MM.Layout.ALL.push(layout);
	return layout;
}

MM.Layout.Graph.update = function(item) {
	var side = this.childDirection;
	if (!item.isRoot()) {
		side = item.getParent().getLayout().getChildDirection(item);
	}
	this._alignItem(item, side);

	this._layoutItem(item, this.childDirection);

	if (this.childDirection == "left" || this.childDirection == "right") {
		this._drawLinesHorizontal(item, this.childDirection);
	} else {
		this._drawLinesVertical(item, this.childDirection);
	}
	return this;
}


/**
 * Generic graph child layout routine. Updates item's orthogonal size according
 * to the sum of its children.
 */
MM.Layout.Graph._layoutItem = function(item, rankDirection) {
	var sizeProps = ["width", "height"];
	var posProps = ["left", "top"];
	var rankIndex = (rankDirection == "left" || rankDirection == "right" ? 0 : 1);
	var childIndex = (rankIndex+1) % 2;

	var rankPosProp = posProps[rankIndex];
	var childPosProp = posProps[childIndex];
	var rankSizeProp = sizeProps[rankIndex];
	var childSizeProp = sizeProps[childIndex];

	var dom = item.getDOM();

	/* content size */
	var contentSize = [dom.content.offsetWidth, dom.content.offsetHeight];

	/* children size */
	var bbox = this._computeChildrenBBox(item.getChildren(), childIndex);

	/* node size */
	var rankSize = contentSize[rankIndex];
	if (bbox[rankIndex]) { rankSize += bbox[rankIndex] + this.SPACING_RANK; }
	var childSize = Math.max(bbox[childIndex], contentSize[childIndex]);
	dom.node.style[rankSizeProp] = rankSize + "px";
	dom.node.style[childSizeProp] = childSize + "px";

	var offset = [0, 0];
	if (rankDirection == "right") { offset[0] = contentSize[0] + this.SPACING_RANK; }
	if (rankDirection == "bottom") { offset[1] = contentSize[1] + this.SPACING_RANK; }
	offset[childIndex] = Math.round((childSize - bbox[childIndex])/2);
	this._layoutChildren(item.getChildren(), rankDirection, offset, bbox);

	/* label position */
	var labelPos = 0;
	if (rankDirection == "left") { labelPos = rankSize - contentSize[0]; }
	if (rankDirection == "top") { labelPos = rankSize - contentSize[1]; }
	dom.content.style[childPosProp] = Math.round((childSize - contentSize[childIndex])/2) + "px";
	dom.content.style[rankPosProp] = labelPos + "px";

	return this;
}

MM.Layout.Graph._layoutChildren = function(children, rankDirection, offset, bbox) {
	var posProps = ["left", "top"];
	var rankIndex = (rankDirection == "left" || rankDirection == "right" ? 0 : 1);
	var childIndex = (rankIndex+1) % 2;
	var rankPosProp = posProps[rankIndex];
	var childPosProp = posProps[childIndex];

	children.forEach(function(child, index) {
		var node = child.getDOM().node;
		var childSize = [node.offsetWidth, node.offsetHeight];

		if (rankDirection == "left") { offset[0] = bbox[0] - childSize[0]; }
		if (rankDirection == "top") { offset[1] = bbox[1] - childSize[1]; }

		node.style[childPosProp] = offset[childIndex] + "px";
		node.style[rankPosProp] = offset[rankIndex] + "px";

		offset[childIndex] += childSize[childIndex] + this.SPACING_CHILD; /*
																			 * offset
																			 * for
																			 * next
																			 * child
																			 */
	}, this);

	return bbox;
}

MM.Layout.Graph._drawLinesHorizontal = function(item, side) {
	this._anchorCanvas(item);
	this._drawHorizontalConnectors(item, side, item.getChildren());
}

MM.Layout.Graph._drawLinesVertical = function(item, side) {
	this._anchorCanvas(item);
	this._drawVerticalConnectors(item, side, item.getChildren());
}

MM.Layout.Graph._drawHorizontalConnectors = function(item, side, children) {
	if (children.length == 0) { return; }

	var dom = item.getDOM();
	var canvas = dom.canvas;
	var ctx = canvas.getContext("2d");
	ctx.strokeStyle = item.getColor();
	var R = this.SPACING_RANK/2;

	/* first part */
	var y1 = item.getShape().getVerticalAnchor(item);
	if (side == "left") {
		var x1 = dom.content.offsetLeft - 0.5;
	} else {
		var x1 = dom.content.offsetWidth + dom.content.offsetLeft + 0.5;
	}
	
	this._anchorToggle(item, x1, y1, side);
	if (item.isCollapsed()) { return; }

	if (children.length == 1) {
		var child = children[0];
		var y2 = child.getShape().getVerticalAnchor(child) + child.getDOM().node.offsetTop;
		var x2 = this._getChildAnchor(child, side);
		ctx.beginPath();
		ctx.moveTo(x1, y1);
		ctx.bezierCurveTo((x1+x2)/2, y1, (x1+x2)/2, y2, x2, y2);
		ctx.stroke();
		return;
	}

	if (side == "left") {
		var x2 = x1 - R;
	} else {
		var x2 = x1 + R;
	}

	ctx.beginPath();
	ctx.moveTo(x1, y1);
	ctx.lineTo(x2, y1);
	ctx.stroke();

	/* rounded connectors */
	var c1 = children[0];
	var c2 = children[children.length-1];
 	var x = x2;
 	var xx = x + (side == "left" ? -R : R);

	var y1 = c1.getShape().getVerticalAnchor(c1) + c1.getDOM().node.offsetTop;
	var y2 = c2.getShape().getVerticalAnchor(c2) + c2.getDOM().node.offsetTop;
	var x1 = this._getChildAnchor(c1, side);
	var x2 = this._getChildAnchor(c2, side);

	ctx.beginPath();
	ctx.moveTo(x1, y1);
	ctx.lineTo(xx, y1)
	ctx.arcTo(x, y1, x, y1+R, R);
	ctx.lineTo(x, y2-R);
	ctx.arcTo(x, y2, xx, y2, R);
	ctx.lineTo(x2, y2);

	for (var i=1; i<children.length-1; i++) {
		var c = children[i];
		var y = c.getShape().getVerticalAnchor(c) + c.getDOM().node.offsetTop;
		ctx.moveTo(x, y);
		ctx.lineTo(this._getChildAnchor(c, side), y);
	}
	ctx.stroke();
}

MM.Layout.Graph._drawVerticalConnectors = function(item, side, children) {
	if (children.length == 0) { return; }
	console.debug(JSON.stringify(item));
	var dom = item.getDOM();
	var canvas = dom.canvas;
	var ctx = canvas.getContext("2d");
	ctx.strokeStyle = item.getColor();

	/* first part */
	var R = this.SPACING_RANK/2;
	
	var x = item.getShape().getHorizontalAnchor(item);
	var height = (children.length == 1 ? 2*R : R);

	if (side == "top") {
		var y1 = canvas.height - dom.content.offsetHeight;
		var y2 = y1 - height;
		this._anchorToggle(item, x, y1, side);
	} else {
		var y1 = item.getShape().getVerticalAnchor(item);
		var y2 = dom.content.offsetHeight + height;
		this._anchorToggle(item, x, dom.content.offsetHeight, side);
	}
	ctx.beginPath();
	ctx.moveTo(x, y1);
	ctx.lineTo(x, y2);
	// ctx.arc(70,60,5,0,2*Math.PI);
	console.debug(item._dom.content.offsetHeight+"height");
	console.debug(item._dom.content.offsetWidth+"width");
	ctx.stroke();

	if (children.length == 1) {	return; }

	/* rounded connectors */
	var c1 = children[0];
	var c2 = children[children.length-1];
	var offset = dom.content.offsetHeight + height;
	var y = Math.round(side == "top" ? canvas.height - offset : offset) + 0.5;

	var x1 = c1.getShape().getHorizontalAnchor(c1) + c1.getDOM().node.offsetLeft;
	var x2 = c2.getShape().getHorizontalAnchor(c2) + c2.getDOM().node.offsetLeft;
	var y1 = this._getChildAnchor(c1, side);
	var y2 = this._getChildAnchor(c2, side);

	ctx.beginPath();
	ctx.moveTo(x1, y1);
	ctx.arcTo(x1, y, x1+R, y, R);
	ctx.lineTo(x2-R, y);
	
	ctx.arcTo(x2, y, x2, y2, R);
	ctx.lineTo(x2,y2);/* 加入 线条 by xiaxuanyu */

	for (var i=1; i<children.length-1; i++) {
		var c = children[i];
		var x = c.getShape().getHorizontalAnchor(c) + c.getDOM().node.offsetLeft;
		ctx.moveTo(x, y);
		ctx.lineTo(x, this._getChildAnchor(c, side));
	}
	ctx.stroke();
}


MM.Layout.Graph.Down = MM.Layout.Graph.create("bottom", "graph-bottom", "Bottom");
MM.Layout.Graph.Up = MM.Layout.Graph.create("top", "graph-top", "Top");
MM.Layout.Graph.Left = MM.Layout.Graph.create("left", "graph-left", "Left");
MM.Layout.Graph.Right = MM.Layout.Graph.create("right", "graph-right", "Right");
MM.Layout.Tree = Object.create(MM.Layout, {
	SPACING_RANK: {value: 32},
	childDirection: {value: ""}
});

MM.Layout.Tree.getChildDirection = function(child) {
	return this.childDirection;
}

MM.Layout.Tree.create = function(direction, id, label) {
	var layout = Object.create(this, {
		childDirection: {value:direction},
		id: {value:id},
		label: {value:label}
	});
	MM.Layout.ALL.push(layout);
	return layout;
}

MM.Layout.Tree.update = function(item) {
	var side = this.childDirection;
	if (!item.isRoot()) {
		side = item.getParent().getLayout().getChildDirection(item);
	}
	this._alignItem(item, side);

	this._layoutItem(item, this.childDirection);
	this._anchorCanvas(item);
	this._drawLines(item, this.childDirection);
	return this;
}

/**
 * Generic graph child layout routine. Updates item's orthogonal size according
 * to the sum of its children.
 */
MM.Layout.Tree._layoutItem = function(item, rankDirection) {
	var dom = item.getDOM();

	/* content size */
	var contentSize = [dom.content.offsetWidth, dom.content.offsetHeight];

	/* children size */
	var bbox = this._computeChildrenBBox(item.getChildren(), 1);

	/* node size */
	var rankSize = contentSize[0];
	var childSize = bbox[1] + contentSize[1];
	if (bbox[0]) { 
		rankSize = Math.max(rankSize, bbox[0] + this.SPACING_RANK); 
		childSize += this.SPACING_CHILD;
	}
	dom.node.style.width = rankSize + "px";
	dom.node.style.height = childSize + "px";

	var offset = [this.SPACING_RANK, contentSize[1]+this.SPACING_CHILD];
	if (rankDirection == "left") { offset[0] = rankSize - bbox[0] - this.SPACING_RANK; }
	this._layoutChildren(item.getChildren(), rankDirection, offset, bbox);

	/* label position */
	var labelPos = 0;
	if (rankDirection == "left") { labelPos = rankSize - contentSize[0]; }
	dom.content.style.left = labelPos + "px";
	dom.content.style.top = 0;

	return this;
}

MM.Layout.Tree._layoutChildren = function(children, rankDirection, offset, bbox) {
	children.forEach(function(child, index) {
		var node = child.getDOM().node;
		var childSize = [node.offsetWidth, node.offsetHeight];
		var left = offset[0];
		if (rankDirection == "left") { left += (bbox[0] - childSize[0]); }

		node.style.left = left + "px";
		node.style.top = offset[1] + "px";

		offset[1] += childSize[1] + this.SPACING_CHILD; /* offset for next child */
	}, this);

	return bbox;
}

MM.Layout.Tree._drawLines = function(item, side) {
	var dom = item.getDOM();
	var canvas = dom.canvas;

	var R = this.SPACING_RANK/4;
	var x = (side == "left" ? canvas.width - 2*R : 2*R) + 0.5;
	this._anchorToggle(item, x, dom.content.offsetHeight, "bottom");

	var children = item.getChildren();
	if (children.length == 0 || item.isCollapsed()) { return; }

	var ctx = canvas.getContext("2d");
	ctx.strokeStyle = item.getColor();

	var y1 = item.getShape().getVerticalAnchor(item);
	var last = children[children.length-1];
	var y2 = last.getShape().getVerticalAnchor(last) + last.getDOM().node.offsetTop;

	ctx.beginPath();
	ctx.moveTo(x, y1);
	ctx.lineTo(x, y2 - R);

	/* rounded connectors */
	for (var i=0; i<children.length; i++) {
		var c = children[i];
		var y = c.getShape().getVerticalAnchor(c) + c.getDOM().node.offsetTop;
		var anchor = this._getChildAnchor(c, side);

		ctx.moveTo(x, y - R);
		ctx.arcTo(x, y, anchor, y, R);
		ctx.lineTo(anchor, y);
	}
	ctx.stroke();
}

MM.Layout.Tree.Left = MM.Layout.Tree.create("left", "tree-left", "Left");
MM.Layout.Tree.Right = MM.Layout.Tree.create("right", "tree-right", "Right");
MM.Layout.Map = Object.create(MM.Layout.Graph, {
	id: {value:"map"},
	label: {value:"Map"},
	LINE_THICKNESS: {value:8}
});
MM.Layout.ALL.push(MM.Layout.Map);

MM.Layout.Map.update = function(item) {
	if (item.isRoot()) {
		this._layoutRoot(item);
	} else {
		var side = this.getChildDirection(item);
		var name = side.charAt(0).toUpperCase() + side.substring(1);
		MM.Layout.Graph[name].update(item);
	}
}

/**
 * @param {MM.Item}
 *            child Child node
 */
MM.Layout.Map.getChildDirection = function(child) {
	while (!child.getParent().isRoot()) {
		child = child.getParent();
	}
	/* child is now the sub-root node */

	var side = child.getSide();
	if (side) { return side; }

	var counts = {left:0, right:0};
	var children = child.getParent().getChildren();
	for (var i=0;i<children.length;i++) {
		var side = children[i].getSide();
		if (!side) {
			side = (counts.right > counts.left ? "left" : "right");
			children[i].setSide(side);
		}
		counts[side]++;
	}

	return child.getSide();
}

MM.Layout.Map.pickSibling = function(item, dir) {
	if (item.isRoot()) { return item; }

	var parent = item.getParent();
	var children = parent.getChildren();
	if (parent.isRoot()) {
		var side = this.getChildDirection(item);
		children = children.filter(function(child) {
			return (this.getChildDirection(child) == side);
		}, this);
	}
	
	var index = children.indexOf(item);
	index += dir;
	index = (index+children.length) % children.length;
	return children[index];
}

MM.Layout.Map._layoutRoot = function(item) {
	this._alignItem(item, "right");

	var dom = item.getDOM();

	var children = item.getChildren();
	var childrenLeft = [];
	var childrenRight = [];

	children.forEach(function(child, index) {
		var node = child.getDOM().node;
		var side = this.getChildDirection(child);
		
		if (side == "left") {
			childrenLeft.push(child);
		} else {
			childrenRight.push(child);
		}
	}, this);

	var bboxLeft = this._computeChildrenBBox(childrenLeft, 1);
	var bboxRight = this._computeChildrenBBox(childrenRight, 1);
	var height = Math.max(bboxLeft[1], bboxRight[1], dom.content.offsetHeight);

	var left = 0;
	this._layoutChildren(childrenLeft, "left", [left, Math.round((height-bboxLeft[1])/2)], bboxLeft);
	left += bboxLeft[0];

	if (childrenLeft.length) { left += this.SPACING_RANK; }
	dom.content.style.left = left + "px";
	left += dom.content.offsetWidth;

	if (childrenRight.length) { left += this.SPACING_RANK; }
	this._layoutChildren(childrenRight, "right", [left, Math.round((height-bboxRight[1])/2)], bboxRight);
	left += bboxRight[0];

	dom.content.style.top = Math.round((height - dom.content.offsetHeight)/2) + "px";
	dom.node.style.height = height + "px";
	dom.node.style.width = left + "px";
	this._anchorCanvas(item);
	this._drawRootConnectors(item, "left", childrenLeft);
	this._drawRootConnectors(item, "right", childrenRight);
}

MM.Layout.Map._drawRootConnectors = function(item, side, children) {
	if (children.length == 0 || item.isCollapsed()) { return; }

	var dom = item.getDOM();
	var canvas = dom.canvas;
	var ctx = canvas.getContext("2d");
	var R = this.SPACING_RANK/2;

	var x1 = dom.content.offsetLeft + dom.content.offsetWidth/2;
	var y1 = item.getShape().getVerticalAnchor(item);
	var half = this.LINE_THICKNESS/2;
	for (var i=0;i<children.length;i++) {
		var child = children[i];

		var x2 = this._getChildAnchor(child, side);
		var y2 = child.getShape().getVerticalAnchor(child) + child.getDOM().node.offsetTop;
		var angle = Math.atan2(y2-y1, x2-x1) + Math.PI/2;
		var dx = Math.cos(angle) * half;
		var dy = Math.sin(angle) * half;
		ctx.fillStyle = ctx.strokeStyle = child.getColor();
		ctx.beginPath();
		ctx.moveTo(x1-dx, y1-dy);
		ctx.quadraticCurveTo((x2+x1)/2, y2, x2, y2);
		ctx.quadraticCurveTo((x2+x1)/2, y2, x1+dx, y1+dy);
		ctx.fill();
		ctx.stroke();
	}

}
MM.Shape = Object.create(MM.Repo, {
	VERTICAL_OFFSET: {value: 0.5},
});

MM.Shape.set = function(item) {
	item.getDOM().node.classList.add("shape-"+this.id);
	return this;
}

MM.Shape.unset = function(item) {
	item.getDOM().node.classList.remove("shape-"+this.id);
	return this;
}

MM.Shape.update = function(item) {
	item.getDOM().content.style.borderColor = item.getColor();
	item.getDOM().content.style.backgroundColor = item.getNodeColor();/*
																		 * 修改节点背景色
																		 * by
																		 * xiaxuanyu
																		 */
	item.getDOM().text.style.color = item.getFontColor();/*
															 * 修改字体颜色 by
															 * xiaxuanyu
															 */
	
	return this;
}

MM.Shape.getHorizontalAnchor = function(item) {
	var node = item.getDOM().content;
	return Math.round(node.offsetLeft + node.offsetWidth/2) + 0.5;
}

MM.Shape.getVerticalAnchor = function(item) {
	var node = item.getDOM().content;
	return node.offsetTop + Math.round(node.offsetHeight * this.VERTICAL_OFFSET) + 0.5;
}
MM.Shape.Underline = Object.create(MM.Shape, {
	id: {value: "underline"},
	label: {value: "Underline"},
	VERTICAL_OFFSET: {value: -3}
});

MM.Shape.Underline.update = function(item) {
	var dom = item.getDOM();

	var ctx = dom.canvas.getContext("2d");
	ctx.strokeStyle = item.getColor();
	dom.text.style.color = item.getFontColor();/* 修改下划线字体颜色 by xiaxuanyu */
	dom.content.style.backgroundColor = item.getNodeColor();/*
															 * 修改下划线背景颜色 by
															 * xiaxuanyu
															 */
	var left = dom.content.offsetLeft;
	var right = left + dom.content.offsetWidth;

	var top = this.getVerticalAnchor(item);

	ctx.beginPath();
	ctx.moveTo(left, top);
	ctx.lineTo(right, top);
	ctx.stroke();
}

MM.Shape.Underline.getVerticalAnchor = function(item) {
	var node = item.getDOM().content;
	return node.offsetTop + node.offsetHeight + this.VERTICAL_OFFSET + 0.5;
}
MM.Shape.Box = Object.create(MM.Shape, {
	id: {value: "box"},
	label: {value: "Box"}
});
MM.Shape.Ellipse = Object.create(MM.Shape, {
	id: {value: "ellipse"},
	label: {value: "Ellipse"}
});
/* 添加左右三角图形的类 by xiaxuanyu start */
MM.Shape.Triangle_right = Object.create(MM.Shape, {
	id: {value: "triangle_right"},
	label: {value: "Triangle_right"}
});
MM.Shape.Triangle_left = Object.create(MM.Shape, {
	id: {value: "triangle_left"},
	label: {value: "Triangle_left"}
});
/* end */
MM.Format = Object.create(MM.Repo, {
	extension: {value:""},
	mime: {value:""}
});

MM.Format.getByName = function(name) {
	var index = name.lastIndexOf(".");
	if (index == -1) { return null; }
	var extension = name.substring(index+1).toLowerCase(); 
	return this.getByProperty("extension", extension);
}

MM.Format.getByMime = function(mime) {
	return this.getByProperty("mime", mime);
}

MM.Format.to = function(data) {}
MM.Format.from = function(data) {}
MM.Format.JSON = Object.create(MM.Format, {
	id: {value: "json"},
	label: {value: "Native (JSON)"},
	extension: {value: "mymind"},
	mime: {value: "application/vnd.mymind+json"}
});

MM.Format.JSON.to = function(data) { 
	return JSON.stringify(data, null, 2) + "\n";
}

MM.Format.JSON.from = function(data) {
	return JSON.parse(data);
}
MM.Format.FreeMind = Object.create(MM.Format, {
	id: {value: "freemind"},
	label: {value: "FreeMind"},
	extension: {value: "mm"},
	mime: {value: "application/x-freemind"}
});

MM.Format.FreeMind.to = function(data) { 
	var doc = document.implementation.createDocument(null, null);
	var map = doc.createElement("map");

	map.setAttribute("version", "0.9.0");
	map.appendChild(this._serializeItem(doc, data.root));

	doc.appendChild(map);
	var serializer = new XMLSerializer();
	return serializer.serializeToString(doc);
}

MM.Format.FreeMind.from = function(data) {
	var parser = new DOMParser();
	var doc = parser.parseFromString(data, "application/xml");
	if (doc.documentElement.nodeName.toLowerCase() == "parsererror") { throw new Error(doc.documentElement.textContent); }

	var root = doc.documentElement.getElementsByTagName("node")[0];
	if (!root) { throw new Error("No root node found"); }

	var json = {
		root: this._parseNode(root, {shape:"underline"})
	};
	json.root.layout = "map";
	json.root.shape = "ellipse";

	return json;
}

MM.Format.FreeMind._serializeItem = function(doc, json) {
	var elm = this._serializeAttributes(doc, json);

	(json.children || []).forEach(function(child) {
		elm.appendChild(this._serializeItem(doc, child));
	}, this);

	return elm;
}

MM.Format.FreeMind._serializeAttributes = function(doc, json) {
	var elm = doc.createElement("node");
	elm.setAttribute("TEXT", json.text);
	elm.setAttribute("ID", json.id);

	if (json.side) { elm.setAttribute("POSITION", json.side); }
	if (json.shape == "box") { elm.setAttribute("STYLE", "bubble"); }
	if (json.collapsed) { elm.setAttribute("FOLDED", "true"); }

	return elm;
}

MM.Format.FreeMind._parseNode = function(node, parent) {
	var json = this._parseAttributes(node, parent);

	for (var i=0;i<node.childNodes.length;i++) {
		var child = node.childNodes[i];
		if (child.nodeName.toLowerCase() == "node") {
			json.children.push(this._parseNode(child, json));
		}
	}

	return json;
}

MM.Format.FreeMind._parseAttributes = function(node, parent) {
	var json = {
		children: [],
		text: node.getAttribute("TEXT") || "",
		id: node.getAttribute("ID")
	};

	var position = node.getAttribute("POSITION");
	if (position) { json.side = position; }

	var style = node.getAttribute("STYLE");
	if (style == "bubble") {
		json.shape = "box";
	} else {
		json.shape = parent.shape;
	}

	if (node.getAttribute("FOLDED") == "true") { json.collapsed = 1; }

	var children = node.children;
	for (var i=0;i<children.length;i++) {
		var child = children[i];
		switch (child.nodeName.toLowerCase()) {
			case "richcontent":
				var body = child.querySelector("body > *");
				if (body) {
					var serializer = new XMLSerializer();
					json.text = serializer.serializeToString(body).trim();
				}
			break;

			case "font":
				if (child.getAttribute("ITALIC") == "true") { json.text = "<i>" + json.text + "</i>"; }
				if (child.getAttribute("BOLD") == "true") { json.text = "<b>" + json.text + "</b>"; }
			break;
		}
	}

	return json;
}
MM.Format.MMA = Object.create(MM.Format.FreeMind, {
	id: {value: "mma"},
	label: {value: "Mind Map Architect"},
	extension: {value: "mma"}
});

MM.Format.MMA._parseAttributes = function(node, parent) {
	var json = {
		children: [],
		text: node.getAttribute("title") || "",
		shape: "box"
	};

	if (node.getAttribute("expand") == "false") { json.collapsed = 1; }

	var direction = node.getAttribute("direction");
	if (direction == "0") { json.side = "left"; }
	if (direction == "1") { json.side = "right"; }

	var color = node.getAttribute("color");
	if (color) {
		var re = color.match(/^#(....)(....)(....)$/);
		if (re) {
			var r = parseInt(re[1], 16) >> 8;
			var g = parseInt(re[2], 16) >> 8;
			var b = parseInt(re[3], 16) >> 8;
			r = Math.round(r/17).toString(16);
			g = Math.round(g/17).toString(16);
			b = Math.round(b/17).toString(16);
		}
		json.color = "#" + [r,g,b].join("");
	}


	return json;
}

MM.Format.MMA._serializeAttributes = function(doc, json) {
	var elm = doc.createElement("node");
	elm.setAttribute("title", json.text);
	elm.setAttribute("expand", json.collapsed ? "false" : "true");

	if (json.side) { elm.setAttribute("direction", json.side == "left" ? "0" : "1"); }
	if (json.color) {
		var parts = json.color.match(/^#(.)(.)(.)$/);
		var r = new Array(5).join(parts[1]);
		var g = new Array(5).join(parts[2]);
		var b = new Array(5).join(parts[3]);
		elm.setAttribute("color", "#" + [r,g,b].join(""));
	}

	return elm;
}
MM.Format.Mup = Object.create(MM.Format, {
	id: {value: "mup"},
	label: {value: "MindMup"},
	extension: {value: "mup"}
});

MM.Format.Mup.to = function(data) {
	var root = this._MMtoMup(data.root);
	return JSON.stringify(root, null, 2);
}

MM.Format.Mup.from = function(data) {
	var source = JSON.parse(data);
	var root = this._MupToMM(source);
	root.layout = "map";

	var map = {
		root: root
	}

	return map;
}

MM.Format.Mup._MupToMM = function(item) {
	var json = {
		text: item.title,
		id: item.id,
		shape: "box"
	}

	if (item.attr && item.attr.style && item.attr.style.background) {
		json.color = item.attr.style.background;
	}

	if (item.attr && item.attr.collapsed) {
		json.collapsed = 1;
	}

	if (item.ideas) {
		var data = [];
		for (var key in item.ideas) {
			var child = this._MupToMM(item.ideas[key]);
			var num = parseFloat(key);
			child.side = (num < 0 ? "left" : "right");
			data.push({
				child: child,
				num: num
			});
		}

		data.sort(function(a, b) {
			return a.num-b.num;
		});

		json.children = data.map(function(item) { return item.child; });
	}

	return json;
}

MM.Format.Mup._MMtoMup = function(item, side) {
	var result = {
		id: item.id,
		title: item.text,
		attr: {}
	}
	if (item.color) {
		result.attr.style = {background:item.color};
	}
	if (item.collapsed) {
		result.attr.collapsed = true;
	}

	if (item.children) {
		result.ideas = {};

		for (var i=0;i<item.children.length;i++) {
			var child = item.children[i];
			var childSide = side || child.side;

			var key = i+1;
			if (childSide == "left") { key *= -1; }

			result.ideas[key] = this._MMtoMup(child, childSide);
		}
	}

	return result;
}
MM.Backend = Object.create(MM.Repo);

/**
 * Backends are allowed to have some internal state. This method notifies them
 * that "their" map is no longer used (was either replaced by a new one or saved
 * using other backend).
 */ 
MM.Backend.reset = function() {
}

MM.Backend.save = function(data, name) {
}

MM.Backend.load = function(name) {
}
MM.Backend.Local = Object.create(MM.Backend, {
	label: {value: "Browser storage"},
	id: {value: "local"},
	prefix: {value: "mm.map."}
});

MM.Backend.Local.save = function(data, id, name) {
	localStorage.setItem(this.prefix + id, data);

	var names = this.list();
	names[id] = name;
	localStorage.setItem(this.prefix + "names", JSON.stringify(names));
}

MM.Backend.Local.load = function(id) {
	var data = localStorage.getItem(this.prefix + id);
	if (!data) { throw new Error("There is no such saved map"); }
	return data;
}

MM.Backend.Local.remove = function(id) {
	localStorage.removeItem(this.prefix + id);

	var names = this.list();
	delete names[id];
	localStorage.setItem(this.prefix + "names", JSON.stringify(names));
}

MM.Backend.Local.list = function() {
	try {
		var data = localStorage.getItem(this.prefix + "names") || "{}";
		return JSON.parse(data);
	} catch (e) {
		return {};
	}
}
MM.Backend.WebDAV = Object.create(MM.Backend, {
	id: {value: "webdav"},
	label: {value: "Generic WebDAV"}
});

MM.Backend.WebDAV.save = function(data, url) {
	return this._request("post", url, data);
}

MM.Backend.WebDAV.load = function(url) {
	return this._request("get", url);
}

MM.Backend.WebDAV._request = function(method, url, data) {
	var xhr = new XMLHttpRequest();
	xhr.open(method, url, true);
	xhr.withCredentials = true;

	var promise = new Promise();
	
	Promise.send(xhr, data).then(
		function(xhr) { promise.fulfill(xhr.responseText); },
		function(xhr) { promise.reject(new Error("HTTP/" + xhr.status + "\n\n" + xhr.responseText)); }
	);

	return promise;
}
MM.Backend.Image = Object.create(MM.Backend, {
	id: {value: "image"},
	label: {value: "Image"},
	url: {value:"", writable:true}
});

MM.Backend.Image.save = function(data, name) {
	var form = document.createElement("form");
	form.action = this.url;
	form.method = "post";
	form.target = "_blank";

	var input = document.createElement("input");
	input.type = "hidden";
	input.name = "data";
	input.value = data;
	form.appendChild(input);

	var input = document.createElement("input");
	input.type = "hidden";
	input.name = "name";
	input.value = name;
	form.appendChild(input);

	document.body.appendChild(form);
	form.submit();
	form.parentNode.removeChild(form);
}
MM.Backend.File = Object.create(MM.Backend, {
	id: {value: "file"},
	label: {value: "File"},
	input: {value:document.createElement("input")}
});

MM.Backend.File.save = function(data, name) {
	var link = document.createElement("a");
	link.download = name;
	link.href = "data:text/plain;base64," + btoa(unescape(encodeURIComponent(data)));
	document.body.appendChild(link);
	link.click();
	link.parentNode.removeChild(link);

	var promise = new Promise().fulfill();
	return promise;
}

MM.Backend.File.load = function() {
	var promise = new Promise();

	this.input.type = "file";

	this.input.onchange = function(e) {
		var file = e.target.files[0];
		if (!file) { return; }

		var reader = new FileReader();
		reader.onload = function() { promise.fulfill({data:reader.result, name:file.name}); }
		reader.onerror = function() { promise.reject(reader.error); }
		reader.readAsText(file);
	}.bind(this);

	this.input.click();
	return promise;
}
/*增加数据库保存 by xiaxuanyu start*/
MM.Backend.DataBase = Object.create(MM.Backend, {
	id: {value: "database"},
	label: {value: "DataBase"},
	input: {value:document.createElement("input")}
});

MM.Backend.DataBase.save = function(data, name) {
	var link = document.createElement("a");
	link.download = name;
	link.href = "data:text/plain;base64," + btoa(unescape(encodeURIComponent(data)));
	document.body.appendChild(link);
	link.click();
	link.parentNode.removeChild(link);

	var promise = new Promise().fulfill();
	return promise;
}

MM.Backend.DataBase.load = function() {
	var promise = new Promise();

	this.input.type = "file";

	this.input.onchange = function(e) {
		var file = e.target.files[0];
		if (!file) { return; }

		var reader = new FileReader();
		reader.onload = function() { promise.fulfill({data:reader.result, name:file.name}); }
		reader.onerror = function() { promise.reject(reader.error); }
		reader.readAsText(file);
	}.bind(this);

	this.input.click();
	return promise;
}
/*end*/
MM.Backend.Firebase = Object.create(MM.Backend, {
	label: {value: "Firebase"},
	id: {value: "firebase"},
	ref: {value:null, writable:true}
});

MM.Backend.Firebase.connect = function(server, auth) {
	this.ref = new Firebase("https://" + server + ".firebaseio.com/");
	
	this.ref.child("names").on("value", function(snap) {
		MM.publish("firebase-list", this, snap.val() || {});
	});

	if (auth) {
		return this._login(auth);
	} else {
		var promise = new Promise();
		promise.fulfill();
		return promise;
	}
}

MM.Backend.Firebase.save = function(data, id, name) {
	var promise = new Promise();

	try {
		this.ref.child("names/" + id).set(name);
		this.ref.child("data/" + id).set(data, function(result) {
			if (result) {
				promise.reject(result);
			} else {
				promise.fulfill();
			}
		});
	} catch (e) {
		promise.reject(e);
	}
	return promise;
}

MM.Backend.Firebase.load = function(id) {
	var promise = new Promise();
	
	this.ref.child("data/" + id).once("value", function(snap) {
		var data = snap.val();
		if (data) {
			promise.fulfill(data);
		} else {
			promise.reject(new Error("There is no such saved map"));
		}
	});
	return promise;
}

MM.Backend.Firebase.remove = function(id) {
	var promise = new Promise();

	try {
		this.ref.child("names/" + id).remove();
		this.ref.child("data/" + id).remove(function(result) {
			if (result) {
				promise.reject(result);
			} else {
				promise.fulfill();
			}
		});
	} catch (e) {
		promise.reject(e);
	}

	return promise;
}

MM.Backend.Firebase._login = function(type) {
	var promise = new Promise();

	var auth = new FirebaseSimpleLogin(this.ref, function(error, user) {
		if (error) {
			promise.reject(error);
		} else if (user) {
			promise.fulfill(user);
		}
	});
	auth.login(type);

	return promise;
}
MM.Backend.GDrive = Object.create(MM.Backend, {
	id: {value: "gdrive"},
	label: {value: "Google Drive"},
	scope: {value: "https://www.googleapis.com/auth/drive https://www.googleapis.com/auth/drive.install"},
	clientId: {value: "767837575056-h87qmlhmhb3djhaaqta5gv2v3koa9hii.apps.googleusercontent.com"},
	apiKey: {value: "AIzaSyCzu1qVxlgufneOYpBgDJXN6Z9SNVcHYWM"},
	fileId: {value: null, writable: true}
});

MM.Backend.GDrive.reset = function() {
	this.fileId = null;
}

MM.Backend.GDrive.save = function(data, name, mime) {
	return this._connect().then(
		function() {
			return this._send(data, name, mime);
		}.bind(this)
	);
}

MM.Backend.GDrive._send = function(data, name, mime) {
	var promise = new Promise();

	var path = "/upload/drive/v2/files";
	var method = "POST";
	if (this.fileId) {
		path += "/" + this.fileId;
		method = "PUT";
	}

	var boundary = "b" + Math.random();
	var delimiter = "--" + boundary;
	var body = [
		delimiter,
		"Content-Type: application/json", "",
		JSON.stringify({title:name}),
		delimiter,
		"Content-Type: " + mime, "",
		data,
		delimiter + "--"
	].join("\r\n");

	var request = gapi.client.request({
		path: path,
		method: method,
		headers: {
			"Content-Type": "multipart/mixed; boundary='" + boundary + "'"
		},
		body: body
	});

	request.execute(function(response) {
		if (!response) {
			promise.reject(new Error("Failed to upload to Google Drive"));
		} else if (response.error) {
			promise.reject(response.error);
		} else {
			this.fileId = response.id;
			promise.fulfill();
		}
	}.bind(this));
	
	return promise;
}

MM.Backend.GDrive.load = function(id) {
	return this._connect().then(
		this._load.bind(this, id)
	);
}

MM.Backend.GDrive._load = function(id) {
	this.fileId = id;

	var promise = new Promise();

	var request = gapi.client.request({
		path: "/drive/v2/files/" + this.fileId,
		method: "GET"
	});

	request.execute(function(response) {
		if (response && response.downloadUrl) {
			var xhr = new XMLHttpRequest();
			xhr.open("get", response.downloadUrl, true);
			xhr.setRequestHeader("Authorization", "Bearer " + gapi.auth.getToken().access_token);
			Promise.send(xhr).then(
				function(xhr) { promise.fulfill({data:xhr.responseText, name:response.title, mime:response.mimeType}); },
				function(xhr) { promise.reject(xhr.responseText); }
			);
		} else {
			promise.reject(response && response.error || new Error("Failed to download file"));
		}
	}.bind(this));

	return promise;
}

MM.Backend.GDrive.pick = function() {
	return this._connect().then(
		this._pick.bind(this)
	);
}

MM.Backend.GDrive._pick = function() {
	var promise = new Promise();

	var token = gapi.auth.getToken();
	var formats = MM.Format.getAll();
	var mimeTypes = ["application/json; charset=UTF-8", "application/json"];
	formats.forEach(function(format) {
		if (format.mime) { mimeTypes.unshift(format.mime); }
	});

	var view = new google.picker.DocsView(google.picker.ViewId.DOCS)
		.setMimeTypes(mimeTypes.join(","))
		.setMode(google.picker.DocsViewMode.LIST);

	var picker = new google.picker.PickerBuilder()
		.enableFeature(google.picker.Feature.NAV_HIDDEN)
		.addView(view)
		.setOAuthToken(token.access_token)
		.setDeveloperKey(this.apiKey)
		.setCallback(function(data) {
			switch (data[google.picker.Response.ACTION]) {
				case google.picker.Action.PICKED:
			 		var doc = data[google.picker.Response.DOCUMENTS][0];
			 		promise.fulfill(doc.id);
				break;

				case google.picker.Action.CANCEL:
					promise.fulfill(null);
				break;
			}
		})
		.build();
	picker.setVisible(true);

	return promise;
}

MM.Backend.GDrive._connect = function() {
	if (window.gapi && window.gapi.auth.getToken()) {
		return new Promise().fulfill();
	} else {
		return this._loadGapi().then(this._auth.bind(this));
	}
}

MM.Backend.GDrive._loadGapi = function() {
	var promise = new Promise();
	if (window.gapi) { return promise.fulfill(); }
	
	var script = document.createElement("script");
	var name = ("cb"+Math.random()).replace(".", "");
	window[name] = promise.fulfill.bind(promise);
	script.src = "https://apis.google.com/js/client:picker.js?onload=" + name;
	document.body.appendChild(script);

	return promise;
}

MM.Backend.GDrive._auth = function(forceUI) {
	var promise = new Promise();

	gapi.auth.authorize({
		"client_id": this.clientId,
		"scope": this.scope,
		"immediate": !forceUI
	}, function(token) {
		if (token && !token.error) { /* done */
			promise.fulfill();
		} else if (!forceUI) { /* try again with ui */
			this._auth(true).then(
				promise.fulfill.bind(promise),
				promise.reject.bind(promise)
			);
		} else { /* bad luck */
			promise.reject(token && token.error || new Error("Failed to authorize with Google"));
		}
	}.bind(this));

	return promise;
}
MM.UI = function() {
	this._node = document.querySelector(".ui");
	this._node.addEventListener("click", this);
	
	this._toggle = this._node.querySelector("#toggle");

	this._layout = new MM.UI.Layout();
	this._shape = new MM.UI.Shape();
	this._color = new MM.UI.Color();
	this._value = new MM.UI.Value();
	this._status = new MM.UI.Status();
	this._tag = new MM.UI.Tag();/* by xiaxuanyu */
		
	MM.subscribe("item-change", this);
	MM.subscribe("item-select", this);

	this.toggle();
}

MM.UI.prototype.handleMessage = function(message, publisher) {
	switch (message) {
		case "item-select":
			this._update();
		break;

		case "item-change":
			if (publisher == MM.App.current) { this._update(); }
		break;
	}
}

MM.UI.prototype.handleEvent = function(e) {
	/* blur to return focus back to app commands */
	if (e.target.nodeName.toLowerCase() != "select") { e.target.blur(); }

	if (e.target == this._toggle) {
		this.toggle();
		return;
	}
	
	var node = e.target;
	while (node != document) {
		var command = node.getAttribute("data-command");
		if (command) {
			MM.Command[command].execute();
			return;
		}
		node = node.parentNode;
	}
}

MM.UI.prototype.toggle = function() {
	this._node.classList.toggle("visible");
	MM.publish("ui-change", this);
}


MM.UI.prototype.getWidth = function() {
	return (this._node.classList.contains("visible") ? this._node.offsetWidth : 0);
}

MM.UI.prototype._update = function() {

	this._layout.update();
	this._shape.update();
	this._value.update();
	this._status.update();
	this._tag.update();/* by xiaxuanyu */
	
}
MM.UI.Layout = function() {
	this._select = document.querySelector("#layout");

	this._select.appendChild(MM.Layout.Map.buildOption());

	var label = this._buildGroup("Graph");
	label.appendChild(MM.Layout.Graph.Right.buildOption());
	label.appendChild(MM.Layout.Graph.Left.buildOption());
	label.appendChild(MM.Layout.Graph.Down.buildOption());
	label.appendChild(MM.Layout.Graph.Up.buildOption());

	var label = this._buildGroup("Tree");
	label.appendChild(MM.Layout.Tree.Right.buildOption());
	label.appendChild(MM.Layout.Tree.Left.buildOption());
	
	this._select.addEventListener("change", this);
}

MM.UI.Layout.prototype.update = function() {
	var value = "";
	var layout = MM.App.current.getOwnLayout();
	if (layout) { value = layout.id; }
	this._select.value = value;
	
	this._getOption("").disabled = MM.App.current.isRoot();
	this._getOption(MM.Layout.Map.id).disabled = !MM.App.current.isRoot();
}

MM.UI.Layout.prototype.handleEvent = function(e) {
	var layout = MM.Layout.getById(this._select.value);

	var action = new MM.Action.SetLayout(MM.App.current, layout);
	MM.App.action(action);
}

MM.UI.Layout.prototype._getOption = function(value) {
	return this._select.querySelector("option[value='" + value + "']");
}

MM.UI.Layout.prototype._buildGroup = function(label) {
	var node = document.createElement("optgroup");
	node.label = label;
	this._select.appendChild(node);
	return node;
}
MM.UI.Shape = function() {
	this._select = document.querySelector("#shape");
	
	this._select.appendChild(MM.Shape.Box.buildOption());
	this._select.appendChild(MM.Shape.Ellipse.buildOption());
	this._select.appendChild(MM.Shape.Underline.buildOption());
	/* 向列表中加入可选项 by xiaxuanyu start */
	this._select.appendChild(MM.Shape.Triangle_left.buildOption());
	this._select.appendChild(MM.Shape.Triangle_right.buildOption());
	/* end */
	this._select.addEventListener("change", this);
}

MM.UI.Shape.prototype.update = function() {
	var value = "";
	var shape = MM.App.current.getOwnShape();
	if (shape) { value = shape.id; }

	this._select.value = value;
}

MM.UI.Shape.prototype.handleEvent = function(e) {
	var shape = MM.Shape.getById(this._select.value);

	var action = new MM.Action.SetShape(MM.App.current, shape);
	MM.App.action(action);
}
MM.UI.Value = function() {
	this._select = document.querySelector("#value");
	this._select.addEventListener("change", this);
}

MM.UI.Value.prototype.update = function() {
	var value = MM.App.current.getValue();
	if (value === null) { value = ""; }
	if (typeof(value) == "number") { value = "num" }

	this._select.value = value;
}

MM.UI.Value.prototype.handleEvent = function(e) {
	var value = this._select.value;
	if (value == "num") {
		MM.Command.Value.execute();
	} else {
		var action = new MM.Action.SetValue(MM.App.current, value || null);
		MM.App.action(action);
	}
}

MM.UI.Status = function() {
	this._select = document.querySelector("#status");
	this._select.addEventListener("change", this);
}

MM.UI.Status.prototype.update = function() {
	this._select.value = MM.App.current.getStatus() || "";
}

MM.UI.Status.prototype.handleEvent = function(e) {
	var action = new MM.Action.SetStatus(MM.App.current, this._select.value || null);
	MM.App.action(action);
}
MM.UI.Color = function() {
	this._node = document.querySelector("#color");
	this._node.addEventListener("click", this);

	var items = this._node.querySelectorAll("[data-color]");
	for (var i=0;i<items.length;i++) {
		var item = items[i];
		item.style.backgroundColor = item.getAttribute("data-color");
	}
}

MM.UI.Color.prototype.handleEvent = function(e) {
	e.preventDefault();
	if (!e.target.hasAttribute("data-color")) { return; }
	
	var color = e.target.getAttribute("data-color") || null;
	/* 确定改变颜色的对象 by xiaxuanyu start */
	var type = document.querySelector("#colorType");
	if(type.value == "edge"){
		var action = new MM.Action.SetColor(MM.App.current, color);		
	}
	if(type.value == "node"){
		var action = new MM.Action.SetNodeColor(MM.App.current, color);		
	}
	if(type.value == "font"){
		var action = new MM.Action.SetFontColor(MM.App.current, color);		
	}
	/* end */
	MM.App.action(action);
}

/* 增加标签 处理 by xiaxuanyu start */
MM.UI.Tag = function() {
	this._node = document.querySelector("#tag");
	this._node.addEventListener("click", this);
}
MM.UI.Tag.prototype.update = function() {
	this._node.value = MM.App.current.getTag() || "";
}
MM.UI.Tag.prototype.handleEvent = function(e) {
	e.preventDefault();
	if (!e.target.hasAttribute("data-tag")) { return; }
	
	var tag = e.target.getAttribute("data-tag") || null;
	var action = new MM.Action.SetTag(MM.App.current, tag);
	MM.App.action(action);
}
/* end */

MM.UI.Help = function() {
	this._node = document.querySelector("#help");
	this._map = {
		8: "Backspace",
		9: "Tab",
		13: "↩",
		32: "Spacebar",
		33: "PgUp",
		34: "PgDown",
		35: "End",
		36: "Home",
		37: "←",
		38: "↑",
		39: "→",
		40: "↓",
		45: "Insert",
		46: "Delete",
		65: "A",
		68: "D",
		83: "S",
		87: "W",
		112: "F1",
		113: "F2",
		114: "F3",
		115: "F4",
		116: "F5",
		117: "F6",
		118: "F7",
		119: "F8",
		120: "F9",
		121: "F10",
		"-": "&minus;"
	};
	
	this._build();
}

MM.UI.Help.prototype.toggle = function() {
	this._node.classList.toggle("visible");
}

MM.UI.Help.prototype._build = function() {
	var t = this._node.querySelector(".navigation");
	this._buildRow(t, "Pan");
	this._buildRow(t, "Select");
	this._buildRow(t, "SelectRoot");
	this._buildRow(t, "SelectParent");
	this._buildRow(t, "Center");
	this._buildRow(t, "ZoomIn", "ZoomOut");
	this._buildRow(t, "Fold");

	var t = this._node.querySelector(".manipulation");
	this._buildRow(t, "InsertSibling");
	this._buildRow(t, "InsertChild");
	this._buildRow(t, "Swap");
	this._buildRow(t, "Side");
	this._buildRow(t, "Delete");

	this._buildRow(t, "Copy");
	this._buildRow(t, "Cut");
	this._buildRow(t, "Paste");

	var t = this._node.querySelector(".editing");
	this._buildRow(t, "Value");
	this._buildRow(t, "Yes", "No", "Computed");
	this._buildRow(t, "Edit");
	this._buildRow(t, "Newline");
	this._buildRow(t, "Bold");
	this._buildRow(t, "Italic");
	this._buildRow(t, "Underline");
	this._buildRow(t, "Strikethrough");

	var t = this._node.querySelector(".other");
	this._buildRow(t, "Undo", "Redo");
	this._buildRow(t, "Save");
	this._buildRow(t, "SaveAs");
	this._buildRow(t, "Load");
	this._buildRow(t, "Help");
	this._buildRow(t, "UI");
}

MM.UI.Help.prototype._buildRow = function(table, commandName) {
	var row = table.insertRow(-1);

	var labels = [];
	var keys = [];

	for (var i=1;i<arguments.length;i++) {
		var command = MM.Command[arguments[i]];
		labels.push(command.label);
		keys = keys.concat(command.keys.map(this._formatKey, this));
	}

	row.insertCell(-1).innerHTML = labels.join("/");
	row.insertCell(-1).innerHTML = keys.join("/");

}

MM.UI.Help.prototype._formatKey = function(key) {
	var str = "";
	if (key.ctrlKey) { str += "Ctrl+"; }
	if (key.altKey) { str += "Alt+"; }
	if (key.shiftKey) { str += "Shift+"; }
	if (key.charCode) { 
		var ch = String.fromCharCode(key.charCode);
		str += this._map[ch] || ch.toUpperCase(); 
	}
	if (key.keyCode) { str += this._map[key.keyCode] || String.fromCharCode(key.keyCode); }
	return str;
}
MM.UI.IO = function() {
	this._prefix = "mm.app.";
	this._mode = "";
	this._node = document.querySelector("#io");
	this._heading = this._node.querySelector("h3");

	this._backend = this._node.querySelector("#backend");
	this._currentBackend = null;
	this._backends = {};
	var ids = ["local", "firebase", "gdrive", "file", "webdav", "image", "database"];
	ids.forEach(function(id) {
		var ui = MM.UI.Backend.getById(id);
		ui.init(this._backend);
		this._backends[id] = ui;
	}, this);

	this._backend.value = localStorage.getItem(this._prefix + "backend") || MM.Backend.File.id;
	this._backend.addEventListener("change", this);
	
	MM.subscribe("map-new", this);
	MM.subscribe("save-done", this);
	MM.subscribe("load-done", this);
}

MM.UI.IO.prototype.restore = function() {
	var parts = {};
	location.search.substring(1).split("&").forEach(function(item) {
		var keyvalue = item.split("=");
		parts[decodeURIComponent(keyvalue[0])] = decodeURIComponent(keyvalue[1]);
	});
	
	/* backwards compatibility */
	if ("map" in parts) { parts.url = parts.map; }

	/* just URL means webdav backend */
	if ("url" in parts && !("b" in parts)) { parts.b = "webdav"; }

	var backend = MM.UI.Backend.getById(parts.b);
	if (backend) { /* saved backend info */
		backend.setState(parts); 
		return;
	}

	if (parts.state) { /* opened from gdrive */
		try {
			var state = JSON.parse(parts.state);
			if (state.action == "open") {
				state = {
					b: "gdrive",
					id: state.ids[0]
				};
				MM.UI.Backend.GDrive.setState(state);
			} else {
				history.replaceState(null, "", ".");
			}
			return;
		} catch (e) { }
	}
}

MM.UI.IO.prototype.handleMessage = function(message, publisher) {
	switch (message) {
		case "map-new":
			this._setCurrentBackend(null);
		break;
		
		case "save-done":
		case "load-done":
			this.hide();
			this._setCurrentBackend(publisher);
		break;
	}
}

MM.UI.IO.prototype.show = function(mode) {
	this._mode = mode;
	this._node.classList.add("visible");
	this._heading.innerHTML = mode;
	
	this._syncBackend();
	window.addEventListener("keydown", this);
}

MM.UI.IO.prototype.hide = function() {
	this._node.classList.remove("visible");
	document.activeElement && document.activeElement.blur();
	window.removeEventListener("keydown", this);
}

MM.UI.IO.prototype.quickSave = function() {
	if (this._currentBackend) {
		this._currentBackend.save();
	} else {
		this.show("save");
	}
}

MM.UI.IO.prototype.handleEvent = function(e) {
	switch (e.type) {
		case "keydown":
			if (e.keyCode == 27) { this.hide(); }
		break;
		
		case "change":
			this._syncBackend();
		break;
	}
}

MM.UI.IO.prototype._syncBackend = function() {
	var all = this._node.querySelectorAll("div[id]");
	[].slice.apply(all).forEach(function(node) { node.style.display = "none"; });
	
	this._node.querySelector("#" + this._backend.value).style.display = "";
	
	this._backends[this._backend.value].show(this._mode);
}

MM.UI.IO.prototype._setCurrentBackend = function(backend) {
	if (this._currentBackend && this._currentBackend != backend) { this._currentBackend.reset(); }
	
	if (backend) { localStorage.setItem(this._prefix + "backend", backend.id); }
	this._currentBackend = backend;
	try {
		this._updateURL(); /* fails when on file:/// */
	} catch (e) {}
}

MM.UI.IO.prototype._updateURL = function() {
	var data = this._currentBackend && this._currentBackend.getState();
	if (!data) {
		history.replaceState(null, "", ".");
	} else {
		var arr = Object.keys(data).map(function(key) {
			return encodeURIComponent(key)+"="+encodeURIComponent(data[key]);
		});
		history.replaceState(null, "", "?" + arr.join("&"));
	}
}
MM.UI.Backend = Object.create(MM.Repo);

MM.UI.Backend.init = function(select) {
	this._backend = MM.Backend.getById(this.id);
	this._mode = "";
	this._prefix = "mm.app." + this.id + ".";

	this._node = document.querySelector("#" + this.id);
	
	this._cancel = this._node.querySelector(".cancel");
	this._cancel.addEventListener("click", this);

	this._go = this._node.querySelector(".go");
	this._go.addEventListener("click", this);
	
	select.appendChild(this._backend.buildOption());
}

MM.UI.Backend.reset = function() {
	this._backend.reset();
}

MM.UI.Backend.setState = function(data) {
}

MM.UI.Backend.getState = function() {
	return null;
}

MM.UI.Backend.handleEvent = function(e) {
	switch (e.target) {
		case this._cancel:
			MM.App.io.hide();
		break;

		case this._go:
			this._action();
		break;
	}
}

MM.UI.Backend.save = function() {
}

MM.UI.Backend.load = function() {
}

MM.UI.Backend.show = function(mode) {
	this._mode = mode;

	this._go.innerHTML = mode.charAt(0).toUpperCase() + mode.substring(1);

	var all = this._node.querySelectorAll("[data-for]");
	[].concat.apply([], all).forEach(function(node) { node.style.display = "none"; });

	var visible = this._node.querySelectorAll("[data-for~=" + mode + "]");
	[].concat.apply([], visible).forEach(function(node) { node.style.display = ""; });
	
	this._go.focus();
}

MM.UI.Backend._action = function() {
	switch (this._mode) {
		case "save":
			this.save();
		break;
		
		case "load":
			this.load();
		break;
	}
}

MM.UI.Backend._saveDone = function() {
	MM.App.setThrobber(false);
	MM.publish("save-done", this);
}

MM.UI.Backend._loadDone = function(json) {
	MM.App.setThrobber(false);
	try {
		MM.App.setMap(MM.Map.fromJSON(json));
		MM.publish("load-done", this);
	} catch (e) { 
		this._error(e);
	}
}

MM.UI.Backend._error = function(e) {
	MM.App.setThrobber(false);
	alert("IO error: " + e.message);
}

MM.UI.Backend._buildList = function(list, select) {
	var data = [];
	
	for (var id in list) {
		data.push({id:id, name:list[id]});
	}
	
	data.sort(function(a, b) {
		return a.name.localeCompare(b.name);
	});
	
	data.forEach(function(item) {
		var o = document.createElement("option");
		o.value = item.id;
		o.innerHTML = item.name;
		select.appendChild(o);
	});
}
MM.UI.Backend.File = Object.create(MM.UI.Backend, {
	id: {value: "file"}
});

MM.UI.Backend.File.init = function(select) {
	MM.UI.Backend.init.call(this, select);

	this._format = this._node.querySelector(".format");
	this._format.appendChild(MM.Format.JSON.buildOption());
	this._format.appendChild(MM.Format.FreeMind.buildOption());
	this._format.appendChild(MM.Format.MMA.buildOption());
	this._format.appendChild(MM.Format.Mup.buildOption());
	this._format.value = localStorage.getItem(this._prefix + "format") || MM.Format.JSON.id;
}

MM.UI.Backend.File.show = function(mode) {
	MM.UI.Backend.show.call(this, mode);
	
	this._go.innerHTML = (mode == "save" ? "Save" : "Browse");
}

MM.UI.Backend.File._action = function() {
	localStorage.setItem(this._prefix + "format", this._format.value);
	
	MM.UI.Backend._action.call(this);
}

MM.UI.Backend.File.save = function() {
	var format = MM.Format.getById(this._format.value);
	var json = MM.App.map.toJSON();
	var data = format.to(json);
	var name = MM.App.map.getName() + "." + format.extension;
	this._backend.save(data, name).then(
		this._saveDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.File.load = function() {
	this._backend.load().then(
		this._loadDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.File._loadDone = function(data) {
	try {
		var format = MM.Format.getByName(data.name) || MM.Format.JSON;
		var json = format.from(data.data);
	} catch (e) { 
		this._error(e);
	}

	MM.UI.Backend._loadDone.call(this, json);
}
/*增加数据库保存ui by xiaxuanyu start*/
MM.UI.Backend.DataBase = Object.create(MM.UI.Backend, {
	id: {value: "database"}
});

MM.UI.Backend.DataBase.save = function() {
	var name = MM.App.map.getName();
	var json = MM.App.map.toJSON();
	var data = MM.Format.JSON.to(json);

	this._backend.save(data, name);
}

MM.UI.Backend.DataBase.load = null;

/*end*/

MM.UI.Backend.WebDAV = Object.create(MM.UI.Backend, {
	id: {value: "webdav"}
});

MM.UI.Backend.WebDAV.init = function(select) {
	MM.UI.Backend.init.call(this, select);

	this._url = this._node.querySelector(".url");
	this._url.value = localStorage.getItem(this._prefix + "url") || "";
	
	this._current = "";
}

MM.UI.Backend.WebDAV.getState = function() {
	var data = {
		url: this._current
	};
	return data;
}

MM.UI.Backend.WebDAV.setState = function(data) {
	this._load(data.url);
}

MM.UI.Backend.WebDAV.save = function() {
	MM.App.setThrobber(true);

	var map = MM.App.map;
	var url = this._url.value;
	localStorage.setItem(this._prefix + "url", url);

	if (url.charCodeAt(url.length-1) != "/") { url += "/"; }
	url += map.getName() + "." + MM.Format.JSON.extension;

	this._current = url;
	var json = map.toJSON();
	var data = MM.Format.JSON.to(json);

	this._backend.save(data, url).then(
		this._saveDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.WebDAV.load = function() {
	this._load(this._url.value);
}

MM.UI.Backend.WebDAV._load = function(url) {
	this._current = url;
	MM.App.setThrobber(true);

	var lastIndex = url.lastIndexOf("/");
	this._url.value = url.substring(0, lastIndex);
	localStorage.setItem(this._prefix + "url", this._url.value);

	this._backend.load(url).then(
		this._loadDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.WebDAV._loadDone = function(data) {
	try {
		var json = MM.Format.JSON.from(data);
	} catch (e) { 
		this._error(e);
	}

	MM.UI.Backend._loadDone.call(this, json);
}
MM.UI.Backend.Image = Object.create(MM.UI.Backend, {
	id: {value: "image"}
});

MM.UI.Backend.Image.save = function() {
	var name = MM.App.map.getName();
	var json = MM.App.map.toJSON();
	var data = MM.Format.JSON.to(json);

	this._backend.save(data, name);
}

MM.UI.Backend.Image.load = null;
MM.UI.Backend.Local = Object.create(MM.UI.Backend, {
	id: {value: "local"}
});

MM.UI.Backend.Local.init = function(select) {
	MM.UI.Backend.init.call(this, select);
	
	this._list = this._node.querySelector(".list");
	this._remove = this._node.querySelector(".remove");
	this._remove.addEventListener("click", this);
}

MM.UI.Backend.Local.handleEvent = function(e) {
	MM.UI.Backend.handleEvent.call(this, e);

	switch (e.target) {
		case this._remove:
			var id = this._list.value;
			if (!id) { break; } 
			this._backend.remove(id);
			this.show(this._mode);
		break;
	}
}

MM.UI.Backend.Local.show = function(mode) {
	MM.UI.Backend.show.call(this, mode);
	
	this._go.disabled = false;

	if (mode == "load") { 
		var list = this._backend.list();
		this._list.innerHTML = "";
		if (Object.keys(list).length) {
			this._go.disabled = false;
			this._remove.disabled = false;
			this._buildList(list, this._list);
		} else {
			this._go.disabled = true;
			this._remove.disabled = true;
			var o = document.createElement("option");
			o.innerHTML = "(no maps saved)";
			this._list.appendChild(o);
		}
	}
}

MM.UI.Backend.Local.setState = function(data) {
	this._load(data.id);
}

MM.UI.Backend.Local.getState = function() {
	var data = {
		b: this.id,
		id: MM.App.map.getId()
	};
	return data;
}

MM.UI.Backend.Local.save = function() {
	var json = MM.App.map.toJSON();
	var data = MM.Format.JSON.to(json);
	try {
		this._backend.save(data, MM.App.map.getId(), MM.App.map.getName());
		this._saveDone();
	} catch (e) {
		this._error(e);
	}
}

MM.UI.Backend.Local.load = function() {
	this._load(this._list.value);
}

MM.UI.Backend.Local._load = function(id) {
	try {
		var data = this._backend.load(id);
		var json = MM.Format.JSON.from(data);
		this._loadDone(json);
	} catch (e) {
		this._error(e);
	}
}
MM.UI.Backend.Firebase = Object.create(MM.UI.Backend, {
	id: {value: "firebase"}
});

MM.UI.Backend.Firebase.init = function(select) {
	MM.UI.Backend.init.call(this, select);
	
	this._online = false;
	this._list = this._node.querySelector(".list");
	this._server = this._node.querySelector(".server");
	this._server.value = localStorage.getItem(this._prefix + "server") || "my-mind";

	this._auth = this._node.querySelector(".auth");
	this._auth.value = localStorage.getItem(this._prefix + "auth") || "";

	this._remove = this._node.querySelector(".remove");
	this._remove.addEventListener("click", this);

	this._go.disabled = false;
	MM.subscribe("firebase-list", this);
}

MM.UI.Backend.Firebase.setState = function(data) {
	this._connect(data.s, data.a).then(
		this._load.bind(this, data.id),
		this._error.bind(this)
	)
}

MM.UI.Backend.Firebase.getState = function() {
	var data = {
		id: MM.App.map.getId(),
		b: this.id,
		s: this._server.value
	};
	if (this._auth.value) { data.a = this._auth.value; }
	return data;
}

MM.UI.Backend.Firebase.show = function(mode) {
	MM.UI.Backend.show.call(this, mode);
	this._sync();
}

MM.UI.Backend.Firebase.handleEvent = function(e) {
	MM.UI.Backend.handleEvent.call(this, e);

	switch (e.target) {
		case this._remove:
			var id = this._list.value;
			if (!id) { break; }
			MM.App.setThrobber(true);
			this._backend.remove(id).then(
				function() { MM.App.setThrobber(false); },
				this._error.bind(this)
			);
		break;
	}
}

MM.UI.Backend.Firebase.handleMessage = function(message, publisher, data) {
	switch (message) {
		case "firebase-list":
			this._list.innerHTML = "";
			if (Object.keys(data).length) {
				this._buildList(data, this._list);
			} else {
				var o = document.createElement("option");
				o.innerHTML = "(no maps saved)";
				this._list.appendChild(o);
			}
			this._sync();
		break;
	}
}

MM.UI.Backend.Firebase._action = function() {
	if (!this._online) {
		this._connect(this._server.value, this._auth.value);
		return;
	}
	
	MM.UI.Backend._action.call(this);
}

MM.UI.Backend.Firebase.save = function() {
	MM.App.setThrobber(true);

	var map = MM.App.map;
	this._backend.save(map.toJSON(), map.getId(), map.getName()).then(
		this._saveDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.Firebase.load = function() {
	this._load(this._list.value);
}

MM.UI.Backend.Firebase._load = function(id) {
	MM.App.setThrobber(true);

	this._backend.load(id).then(
		this._loadDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.Firebase._connect = function(server, auth) {
	var promise = new Promise();

	this._server.value = server;
	this._auth.value = auth;
	this._server.disabled = true;
	this._auth.disabled = true;

	localStorage.setItem(this._prefix + "server", server);
	localStorage.setItem(this._prefix + "auth", auth || "");

	this._go.disabled = true;
	MM.App.setThrobber(true);

	this._backend.connect(server, auth).then(
		function() {
			this._connected();
			promise.fulfill();
		}.bind(this),
		promise.reject.bind(promise)
	);

	return promise;
}

MM.UI.Backend.Firebase._connected = function() {
	MM.App.setThrobber(false);
	this._online = true;
	this._sync();
}

MM.UI.Backend.Firebase._sync = function() {
	if (!this._online) {
		this._go.innerHTML = "Connect";
		return;
	}

	this._go.disabled = false;
	if (this._mode == "load" && !this._list.value) { this._go.disabled = true; }
	this._go.innerHTML = this._mode.charAt(0).toUpperCase() + this._mode.substring(1);
}

MM.UI.Backend.GDrive = Object.create(MM.UI.Backend, {
	id: {value: "gdrive"}
});

MM.UI.Backend.GDrive.init = function(select) {
	MM.UI.Backend.init.call(this, select);

	this._format = this._node.querySelector(".format");
	this._format.appendChild(MM.Format.JSON.buildOption());
	this._format.appendChild(MM.Format.FreeMind.buildOption());
	this._format.appendChild(MM.Format.MMA.buildOption());
	this._format.appendChild(MM.Format.Mup.buildOption());
	this._format.value = localStorage.getItem(this._prefix + "format") || MM.Format.JSON.id;
}

MM.UI.Backend.GDrive.save = function() {
	MM.App.setThrobber(true);

	var format = MM.Format.getById(this._format.value);
	var json = MM.App.map.toJSON();
	var data = format.to(json);
	var name = MM.App.map.getName();
	var mime = "text/plain";
	
	if (format.mime) {
		mime = format.mime;
	} else {
		name += "." + format.extension;
	}

	this._backend.save(data, name, mime).then(
		this._saveDone.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.GDrive.load = function() {
	MM.App.setThrobber(true);

	this._backend.pick().then(
		this._picked.bind(this),
		this._error.bind(this)
	);
}

MM.UI.Backend.GDrive._picked = function(id) {
	MM.App.setThrobber(false);
	if (!id) { return;  }

	MM.App.setThrobber(true);

	this._backend.load(id).then(
		this._loadDone.bind(this),
		this._error.bind(this)
	)
}

MM.UI.Backend.GDrive.setState = function(data) {
	this._picked(data.id);
}

MM.UI.Backend.GDrive.getState = function() {
	var data = {
		b: this.id,
		id: this._backend.fileId
	};
	return data;
}

MM.UI.Backend.GDrive._loadDone = function(data) {
	try {
		var format = MM.Format.getByMime(data.mime) || MM.Format.getByName(data.name) || MM.Format.JSON;
		var json = format.from(data.data);
	} catch (e) { 
		this._error(e);
	}

	MM.UI.Backend._loadDone.call(this, json);
}
MM.Mouse = {
	TOUCH_DELAY: 500,
	_port: null,
	_cursor: [0, 0],
	_pos: [0, 0], /* ghost pos */
	_mode: "",
	_item: null,
	_ghost: null,
	_oldDragState: null,
	_touchTimeout: null
}

MM.Mouse.init = function(port) {
	this._port = port;
	this._port.addEventListener("touchstart", this);
	this._port.addEventListener("mousedown", this);
	this._port.addEventListener("click", this);
	this._port.addEventListener("dblclick", this);
	this._port.addEventListener("wheel", this);
	this._port.addEventListener("mousewheel", this);
	this._port.addEventListener("contextmenu", this);
}

MM.Mouse.handleEvent = function(e) {
	switch (e.type) {
		case "click":
			var item = MM.App.map.getItemFor(e.target);
			var div = document.getElementById("evaluateIndex");
			if (item) { MM.App.select(item); }
			
			if (e.target.getAttribute("name") == "comment") {
				MM.Command.Comment.execute();
			}
			if (e.target.getAttribute("name") == "evaluate") {
				MM.Command.Evaluate.execute();
			}else if (!MM.App.map.isIn(e.target,div) && div.style.display != "none"){
				evaluateEdit();
			}
		break;
		
		case "dblclick":
			var item = MM.App.map.getItemFor(e.target);
			if (item) { MM.Command.Edit.execute(); }
		break;
		
		case "contextmenu":
			this._endDrag();

			var item = MM.App.map.getItemFor(e.target);
			if (item) {	MM.App.select(item); }

			e.preventDefault();
			MM.Menu.open(e.clientX, e.clientY);
		break;

		case "touchstart":
			if (e.touches.length > 1) { return; }
			e.clientX = e.touches[0].clientX;
			e.clientY = e.touches[0].clientY;
		case "mousedown":
			var item = MM.App.map.getItemFor(e.target);

			if (e.type == "touchstart") { /*
											 * context menu here, after we have
											 * the item
											 */
				this._touchTimeout = setTimeout(function() { 
					item && MM.App.select(item);
					MM.Menu.open(e.clientX, e.clientY);
				}, this.TOUCH_DELAY);
			}

			if (item == MM.App.current && MM.App.editing) { return; }
			if(item){
				
				document.activeElement && document.activeElement.blur();
			}else{
				return ;
			}
			this._startDrag(e, item);
		break;
		
		case "touchmove":
			if (e.touches.length > 1) { return; }
			e.clientX = e.touches[0].clientX;
			e.clientY = e.touches[0].clientY;
			clearTimeout(this._touchTimeout);
		case "mousemove":
			this._processDrag(e);
		break;
		
		case "touchend":
			clearTimeout(this._touchTimeout);
		case "mouseup":
			this._endDrag();
		break;

		case "wheel":
		case "mousewheel":
			var dir = 1;
			if (e.wheelDelta && e.wheelDelta < 0) { dir = -1; }
			if (e.deltaY && e.deltaY > 0) { dir = -1; }
			if(!MM.App.editing){/* 加入可编辑时不能放大的条件 by xiaxuanyu */
				MM.App.adjustFontSize(dir);
			}
		break;
	}
}

MM.Mouse._startDrag = function(e, item) {

	if (e.type == "mousedown") {
		e.preventDefault(); /*
							 * no selections allowed. only for mouse; preventing
							 * touchstart would prevent Safari from emulating
							 * clicks
							 */
		this._port.addEventListener("mousemove", this);
		this._port.addEventListener("mouseup", this);
	} else {
		this._port.addEventListener("touchmove", this);
		this._port.addEventListener("touchend", this);
	}

	this._cursor[0] = e.clientX;
	this._cursor[1] = e.clientY;

	if (item && !item.isRoot()) { 
		this._mode = "drag";
		this._item = item;
	} else {
		this._mode = "pan";
		this._port.style.cursor = "move";
	}
}

MM.Mouse._processDrag = function(e) {
	e.preventDefault();
	var dx = e.clientX - this._cursor[0];
	var dy = e.clientY - this._cursor[1];
	this._cursor[0] = e.clientX;
	this._cursor[1] = e.clientY;

	switch (this._mode) {
		case "drag":
			if (!this._ghost) { 
				this._port.style.cursor = "move";
				this._buildGhost(dx, dy); 
			}
			this._moveGhost(dx, dy);
			var state = this._computeDragState();
			this._visualizeDragState(state);
		break;

		case "pan":
			MM.App.map.moveBy(dx, dy);
		break;
	}
}

MM.Mouse._endDrag = function() {
	this._port.style.cursor = "";
	this._port.removeEventListener("mousemove", this);
	this._port.removeEventListener("mouseup", this);

	if (this._mode == "pan") { return; } /* no cleanup after panning */

	if (this._ghost) {
		var state = this._computeDragState();
		this._finishDragDrop(state);

		this._ghost.parentNode.removeChild(this._ghost);
		this._ghost = null;
	}

	this._item = null;
}

MM.Mouse._buildGhost = function() {
	var content = this._item.getDOM().content;
	this._ghost = content.cloneNode(true);
	this._ghost.classList.add("ghost");
	this._pos[0] = content.offsetLeft;
	this._pos[1] = content.offsetTop;
	content.parentNode.appendChild(this._ghost);
}

MM.Mouse._moveGhost = function(dx, dy) {
	this._pos[0] += dx;
	this._pos[1] += dy;
	this._ghost.style.left = this._pos[0] + "px";
	this._ghost.style.top = this._pos[1] + "px";

	var state = this._computeDragState();
}

MM.Mouse._finishDragDrop = function(state) {
	this._visualizeDragState(null);

	var target = state.item;
	switch (state.result) {
		case "append":
			var action = new MM.Action.MoveItem(this._item, target);
		break;

		case "sibling":
			var index = target.getParent().getChildren().indexOf(target);
			var targetIndex = index + (state.direction == "right" || state.direction == "bottom" ? 1 : 0);
			var action = new MM.Action.MoveItem(this._item, target.getParent(), targetIndex, target.getSide());
		break;

		default:
			return;
		break;
	}

	MM.App.action(action);
}

/**
 * Compute a state object for a drag: current result (""/"append"/"sibling"),
 * parent/sibling, direction
 */
MM.Mouse._computeDragState = function() {
	var rect = this._ghost.getBoundingClientRect();
	var closest = MM.App.map.getClosestItem(rect.left + rect.width/2, rect.top + rect.height/2);
	var target = closest.item;

	var state = {
		result: "",
		item: target,
		direction: ""
	}

	var tmp = target;
	while (!tmp.isRoot()) {
		if (tmp == this._item) { return state; } /* drop on a child or self */
		tmp = tmp.getParent();
	}
	
	var w1 = this._item.getDOM().content.offsetWidth;
	var w2 = target.getDOM().content.offsetWidth;
	var w = Math.max(w1, w2);
	var h1 = this._item.getDOM().content.offsetHeight;
	var h2 = target.getDOM().content.offsetHeight;
	var h = Math.max(h1, h2);

	if (target.isRoot()) { /* append here */
		state.result = "append";
	} else if (Math.abs(closest.dx) < w && Math.abs(closest.dy) < h) { /*
																		 * append
																		 * here
																		 */
		state.result = "append";
	} else {
		state.result = "sibling";
		var childDirection = target.getParent().getLayout().getChildDirection(target);
		var diff = -1 * (childDirection == "top" || childDirection == "bottom" ? closest.dx : closest.dy);

		if (childDirection == "left" || childDirection == "right") {
			state.direction = (closest.dy < 0 ? "bottom" : "top");
		} else {
			state.direction = (closest.dx < 0 ? "right" : "left");
		}
	}

	return state;
}

MM.Mouse._visualizeDragState = function(state) {
	if (this._oldState && state && this._oldState.item == state.item && this._oldState.result == state.result) { return; } /*
																															 * nothing
																															 * changed
																															 */

	if (this._oldDragState) { /* remove old vis */
		var item = this._oldDragState.item;
		var node = item.getDOM().content;
		node.style.boxShadow = "";
	}

	this._oldDragState = state;

	if (state) { /* show new vis */
		var item = state.item;
		var node = item.getDOM().content;

		var x = 0;
		var y = 0;
		var offset = 5;
		if (state.result == "sibling") {
			if (state.direction == "left") { x = -1; }
			if (state.direction == "right") { x = +1; }
			if (state.direction == "top") { y = -1; }
			if (state.direction == "bottom") { y = +1; }
		}
		var spread = (x || y ? -2 : 2);
		node.style.boxShadow = (x*offset) + "px " + (y*offset) + "px 2px " + spread + "px #000";
	}
}

MM.App = {
	keyboard: null,
	current: null,
	editing: false,
	associate: false,
	history: [],
	historyIndex: 0,
	portSize: [0, 0],
	map: null,
	ui: null,
	io: null,
	help: null,
	_port: null,
	_throbber: null,
	_drag: {
		pos: [0, 0],
		item: null,
		ghost: null
	},
	_fontSize: 100,
	
	action: function(action) {
		if (this.historyIndex < this.history.length) { /* remove undoed actions */
			this.history.splice(this.historyIndex, this.history.length-this.historyIndex);
		}
		
		this.history.push(action);
		this.historyIndex++;
		
		action.perform();
		return this;
	},
	
	setMap: function(map) {
		if (this.map) { this.map.hide(); }

		this.history = [];
		this.historyIndex = 0;

		this.map = map;
		this.map.show(this._port);
	},
	
	select: function(item) {
		if (item == this.current) { return; }

		if (this.editing) { MM.Command.Finish.execute(); }

		if (this.current) {
			this.current.getDOM().node.classList.remove("current");
		}
		this.current = item;
		this.current.getDOM().node.classList.add("current");
		this.map.ensureItemVisibility(item);
		MM.publish("item-select", item);
	},
	
	adjustFontSize: function(diff) {
		this._fontSize = Math.max(30, this._fontSize + 10*diff);
		this._port.style.fontSize = this._fontSize + "%";
		this.map.update();
		this.map.ensureItemVisibility(this.current);
		this.map.adjustAssociate();
	},
	
	handleMessage: function(message, publisher) {
		switch (message) {
			case "ui-change":
				this._syncPort();
			break;

			case "item-change":
				if (publisher.isRoot() && publisher.getMap() == this.map) {
					document.title = this.map.getName() + " :: My Mind";
				}
			break;
		}
	},

	handleEvent: function(e) {
		switch (e.type) {
			case "resize":
				this._syncPort();
			break;

			case "beforeunload":
				e.preventDefault();
				return "";
			break;
		}
	},
	
	setThrobber: function(visible) {
		this._throbber.classList[visible ? "add" : "remove"]("visible");
	},

	init: function() {
		this._port = document.querySelector("#port");
		this._throbber = document.querySelector("#throbber");
		this.ui = new MM.UI();
		this.io = new MM.UI.IO();
		this.help = new MM.UI.Help();
		MM.Tip.init();
		MM.Keyboard.init();
		MM.Menu.init(this._port);
		MM.Mouse.init(this._port);

		window.addEventListener("resize", this);
		window.addEventListener("beforeunload", this);
		MM.subscribe("ui-change", this);
		MM.subscribe("item-change", this);
		
		this._syncPort();
		this.setMap(new MM.Map());
	},

	_syncPort: function() {
		this.portSize = [window.innerWidth - this.ui.getWidth(), window.innerHeight];
		this._port.style.width = this.portSize[0] + "px";
		this._port.style.height = this.portSize[1] + "px";
		/* add by xiaxuanyu start */
		var div=document.querySelector("#associatePort");
		div.style.width = this.portSize[0] + "px";
		div.style.height = this.portSize[1] + "px";
		/* end */
		this._throbber.style.right = (20 + this.ui.getWidth())+ "px";
		if (this.map) { this.map.ensureItemVisibility(this.current); }
	}
}

/*评估指标*/
MM.Evaluate = function() {
	this._name = null;
	this._unit = null;
	this._type = null;
}
MM.Evaluate.fromJSON = function(data) {
	return new this().fromJSON(data);
}

/**
 * Only when creating a new item. To merge existing items, use .mergeWith().
 */
MM.Evaluate.prototype.fromJSON = function(data) {

	if (data.name) { this._name = data.name; }
	if (data.unit) { this._unit = data.unit; }
	if (data.type) { this._type = data.type; }
	return this;
}

MM.Evaluate.prototype.toJSON = function() {
	var data = {
		}
	if (this._name) { data.name = this._name; }
	if (this._unit) { data.unit = this._unit; }
	if (this._type) { data.type = this._type; }
	return data;
}
MM.Evaluate.prototype.setName = function(name) {
	this._name = name;
}

MM.Evaluate.prototype.getName = function() {
	return this._name;
}
MM.Evaluate.prototype.setUnit = function(unit) {
	this._unit = unit;
}

MM.Evaluate.prototype.getUnit = function() {
	return this._unit;
}
MM.Evaluate.prototype.setType = function(type) {
	this._type = type;
}

MM.Evaluate.prototype.getType = function() {
	return this._type;
}
/*联系*/
MM.Associate = function() {
	this._parent = null;
	/* MM.Item 类型 */
	this._from = null;
	this._to = null;

	/* up down left right */
	this._fromSide = null;
	this._toSide = null;
	
	/* 长度 */
	this._fromLenght = null;
	this._toLenght = null;
	
	this._color = null;
	/* 线条坐标点集合 */
	this._positionPoints = []; 
	this._id = MM.generateId();
	this._select = null;/*保存被选中的移动线条*/
	this._finish = false;
	
	
}
MM.Associate.COLOR = "#999";

MM.Associate.fromJSON = function(data) {
	return new this().fromJSON(data);
}

/**
 * Only when creating a new item. To merge existing items, use .mergeWith().
 */
MM.Associate.prototype.fromJSON = function(data) {

	if (data.id) { this._id = data.id; }
	if (data.from) { this._from = data.from; }
	if (data.to) { this._to = data.to; }
	if (data.fromSide) { this._fromSide = data.fromSide; }
	if (data.toSide) {this._toSide = data.toSide; }
	if (data.color) { this._color = data.color; }
	if (data.positionPoints) { this._positionPoints = data.positionPoints; }

	return this;
}

MM.Associate.prototype.toJSON = function() {
	var data = {
		id: this._id,
	}
	
	if (this._from) { data.from = this._from._id; }
	if (this._to) { data.to = this._to._id; }
	if (this._fromSide) { data.fromSide = this._fromSide; }
	if (this._toSide) { data.toSide = this._toSide; }
	if (this._color) { data.color = this._color; }
	if (this._positionPoints.length) {
		data.positionPoints = this._positionPoints;
	}

	return data;
}

MM.Associate.prototype.update = function(source) {
	var map = this.getMap();
	if (!map || !map.isVisible()) { return this; }
	var associatePort = document.querySelector("#associatePort");
	var divs = associatePort.querySelectorAll(".lineDiv");
	var arrows = associatePort.querySelectorAll(".lineArrow");
	if(source){//更新单独的一个联系
		var id = source._id;
		if(divs){
			for(var i=0 ;i<divs.length;i++){
				if(divs[i].getAttribute("name") == id){
					associatePort.removeChild(divs[i]);
				}
			}
		}
		if(arrows){
			for(var i=0 ;i<arrows.length;i++){
				if(arrows[i].getAttribute("name") == id){
					associatePort.removeChild(arrows[i]);
				}
			}
		}
		if(source.getFinish()){
			source.startAssociate();
			source.setFinish(false);
		}else{
			source.drawConnector();
		}
	}else{
		var associates = map.getAssociate();
		if(0 < associates.length){
			if(divs){
				for(var i=0;i<divs.length;i++){
					associatePort.removeChild(divs[i]);
				}
			}
			if(arrows){
				for(var i=0;i<arrows.length;i++){
					associatePort.removeChild(arrows[i]);
				}
			}
			associates.forEach(function(associate){
				if(associate.getFrom() && associate.getTo()){
					if(associate.getFinish()){
						associate.startAssociate();
					}else{
						associate.movePosition();
						associate.adjustPosition();
						associate.drawConnector();
					}
				}
			});
		}
	}
	
}


MM.Associate.prototype.setFrom = function(from) {
	this._from = from;
	return this.update();
}

MM.Associate.prototype.getFrom = function() {
	return this._from;
}

MM.Associate.prototype.setFromSide = function(fromSide) {
	this._fromSide = fromSide;
	return this.update();
}

MM.Associate.prototype.getFromSide = function() {
	return this._fromSide;
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

MM.Associate.prototype.setToSide = function(toSide) {
	this._toSide = toSide;
	return this.update();
}

MM.Associate.prototype.getToSide = function() {
	return this._toSide;
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


MM.Associate.prototype.setPositionPoints = function(positionPoints) {
	this._positionPoints = positionPoints;
	return this.update();
}

MM.Associate.prototype.getPositionPoints = function() {
	return this._positionPoints;
}

MM.Associate.prototype.setSelect = function(select) {
	this._select = select;
	return this;
}

MM.Associate.prototype.getSelect = function() {
	return this._select;
}
MM.Associate.prototype.setFinish = function(finish) {
	this._finish = finish;
	return this;
}

MM.Associate.prototype.getFinish = function() {
	return this._finish;
}

MM.Associate.prototype.getMap = function() {
	var item = this._parent;
	while (item) {
		if (item instanceof MM.Map) { return item; }
		item = item.getParent();
	}
	return null;
}
MM.Associate.prototype.setParent = function(parent) {
	this._parent = parent;
}
MM.Associate.prototype.getParent = function() {
	return this._parent;
}
MM.Associate.prototype.getSelectAssociate = function(div) {
	var map = MM.App.map;
	var associates = map.getAssociate();
	var associate = null
	if(div){
		for(var i=0;i<associates.length;i++){
			if(associates[i]._id == div.getAttribute("name")){
				associate = associates[i];
			}
		}

	}else{
		for(var i=0;i<associates.length;i++){
			if(associates[i].getSelect()){
				associate = associates[i];
			}
		}
	}
	return associate;
}

MM.Associate.prototype.select = function(div){
	var name = null;
	if(div){
		name = div.getAttribute("name");
	}else{
		name = this._id;
	}
	var divs = document.querySelectorAll(".lineDiv");
	var arrows = document.querySelectorAll(".lineArrows");
	if(divs){
		for(var i=0;i<divs.length;i++){
			if(name == divs[i].getAttribute("name")){
				
				if(div && div.style.width == "0px"){
					div.classList.add("lineDiv_cursor_left");
				}
				if(div && div.style.height == "0px"){
					div.classList.add("lineDiv_cursor_up");
				}
				divs[i].classList.add("lineDiv_hover");
			}
		}
	}
	if(arrows){
		for(var i=0;i<arrows.length;i++){
			if(name == arrows[i].getAttribute("name")){
				arrows[i].classList.add("lineDiv_hover");
			}
		}
	}
}
MM.Associate.prototype.unSelect = function(div){
	var name = null;
	if(div){
		name = div.getAttribute("name");
	}else{
		name = this._id;
	}
	var divs = document.querySelectorAll(".lineDiv");
	var arrows = document.querySelectorAll(".lineArrows");
	if(divs){
		for(var i=0;i<divs.length;i++){
			if(name == divs[i].getAttribute("name")){
				
				if(div && div.style.width == "0px"){
					div.classList.remove("lineDiv_cursor_left");
				}
				if(div && div.style.height == "0px"){
					div.classList.remove("lineDiv_cursor_up");
				}
				divs[i].classList.remove("lineDiv_hover");
			}
		}
	}
	if(arrows){
		for(var i=0;i<arrows.length;i++){
			if(name == arrows[i].getAttribute("name")){
				arrows[i].classList.remove("lineDiv_hover");
			}
		}
	}
}

MM.Associate.prototype.insertAssociate = function() {
	if(this){
		this.getMap().setAssociate(this);
	}
	var port = document.querySelector("#port");
	
	port.addEventListener("mousemove",this);
	port.addEventListener("click",this);
	return this;
}

MM.Associate.prototype.removeAssociate = function() {
	MM.App.associate = false;
	var divs = associatePort.querySelectorAll(".lineDiv");
	var arrows = associatePort.querySelectorAll(".lineArrow");
	var port = document.querySelector("#port");

	var id = this._id;
	if(divs){
		for(var i=0 ;i<divs.length;i++){
			if(divs[i].getAttribute("name") == id){
				associatePort.removeChild(divs[i]);
			}
		}
	}
	if(arrows){
		for(var i=0 ;i<arrows.length;i++){
			if(arrows[i].getAttribute("name") == id){
				associatePort.removeChild(arrows[i]);
			}
		}
	}
	var map = this.getMap();
	for(var i=0;i<map._associate.length;i++){
		if(this._id == map._associate[i]._id){
			map._associate.splice(i,1);
		}
	}
	this.hideNode();
	port.removeEventListener("click",this);
	port.removeEventListener("mousemove",this);
}

MM.Associate.prototype.startAssociate = function(e) {
	MM.App.associate = true;
	var port = document.querySelector("#port");
	var item = MM.App.current;
	var fromPos = null;
	var toPos = null;
	if(this.getFrom()){
		fromPos = this.computerPosition(this.getFrom(), this.getFromSide());
		fromPos.side = this.getFromSide();
		fromPos.width = this.getFromLength();
	}
	if(this.getTo()){
		toPos = this.computerPosition(this.getTo(), this.getToSide());
		toPos.side = this.getToSide();
		toPos.width = this.getToLength();
		this._positionPoints = this.computerConnectorPoint(fromPos, toPos);
	}else{
		toPos = new Object();
		toPos.X = e.pageX+5;
		toPos.Y = e.pageY+5;
		toPos.side = "down";
		toPos.width = 25;
		this.setPositionPoints(this.computerConnectorPoint(fromPos, toPos));
	}
	this.drawConnector();
	return this;
}

MM.Associate.prototype.stopAssociate = function(e) {
	
	var port = document.querySelector("#port");
	var item = MM.App.map.getItemFor(e.target);
	var root = item.getMap();
	var x = root._root.getDOM().node.offsetLeft;
	var y = root._root.getDOM().node.offsetTop;
	if(this.getFinish()){
		if(item && !item.isRoot()){
			var dom = item.getDOM();
			var tempX = x + dom.node.offsetLeft;
			var tempY = y + dom.node.offsetTop;
			if(e.pageX <= tempX + dom.content.offsetWidth/4){
				this.setToSide("left");
			}else if(e.pageX > tempX + dom.content.offsetWidth*3/4){
				this.setToSide("right");
			}else {
				if(e.pageY <= tempY + dom.content.offsetHeight/2){
					this.setToSide("up");
				}else{
					this.setToSide("down");
				}
			}
			this.setToLength(25);
			this.setTo(item);
			this.setFinish(false);
		}
	}
	MM.App.associate = false;
	port.removeEventListener("click",this);
	port.removeEventListener("mousemove",this);
}
MM.Associate.prototype.selectAssociate = function(e) {
	var div = e.target;
	MM.App.associate = true;
	var divs = document.querySelectorAll(".lineDiv");
	if(divs){
		for(var i=0;i<divs.length;i++){
			if(div.getAttribute("name") == divs[i].getAttribute("name")){
				
				divs[i].removeEventListener("mouseout",this);
				divs[i].addEventListener("mousedown",this);
			}
		}
	}
	var associate = this.getSelectAssociate(div);
	MM.App.select(associate.getFrom());
	associate.select(div);
	associate.setSelect(div);
	this.drawNode();
}

MM.Associate.prototype.unSelectAssociate = function(e) {
	var div = e.target;
	MM.App.associate = false;
	var divs = document.querySelectorAll(".lineDiv");
	if(divs){
		for(var i=0;i<divs.length;i++){
			if(div.getAttribute("name") == divs[i].getAttribute("name")){
				
				divs[i].addEventListener("mouseout",this);
				divs[i].removeEventListener("mousedown",this);
			}
		}
	}
	var associate = this.getSelectAssociate(div);
	associate.setSelect(null);
	this.unSelect(div);
	this.hideNode();
	
}

MM.Associate.prototype.editAssociate = function(e) {
	var div = e.target;
	MM.App.associate = true;
	this.setSelect(div);
	var divs = document.querySelectorAll(".lineDiv");
	var arrows = document.querySelectorAll(".lineArrow");
	if(divs){
		for(var i=0;i<divs.length;i++){
			if(div.getAttribute("name") == divs[i].getAttribute("name")){
				divs[i].classList.add("lineDiv_hover");
				
			}
		}
	}
	if(arrows){
		for(var i=0;i<arrows.length;i++){
			if(div.getAttribute("name") == arrows[i].getAttribute("name")){
				arrows[i].classList.add("lineDiv_hover");
				
			}
		}
	}
	//this.select(div);
	this.drawNode();
}

MM.Associate.prototype.modifyAssociate = function(e) {
	var div = e.target;
	var port = document.querySelector("#port");
	
	div.removeEventListener("mouseout",this);
	port.addEventListener("mousemove",this);
	port.addEventListener("mouseup",this);
}

MM.Associate.prototype.finishAssociate = function(e){
	MM.App.associate = false;
	var div = this.getSelect();
	var port = document.querySelector("#port");
	if(div){
		div.addEventListener("mouseout",this);
		div.removeEventListener("mouseup",this);
	}
	port.removeEventListener("mouseup",this);
	port.removeEventListener("mousemove",this);
	this.setSelect(null);
}

MM.Associate.prototype.stretchAssociate = function(e){
	var div = this.getSelect();
	var port = document.querySelector("#port");
	var index = parseInt(div.getAttribute("num"));
	var positionPoints = this.getPositionPoints();
	if(index > 0 && index < positionPoints.length-2){
		if (div.style.width == "0px"){
			positionPoints[index].X = e.pageX;
			positionPoints[index+1].X = e.pageX;
		}
		if (div.style.height == "0px"){
			positionPoints[index].Y = e.pageY;
			positionPoints[index+1].Y = e.pageY;
		}
		this.update(this);
	}
	
	
}

MM.Associate.prototype.overAssociate = function(e) {
	var div = e.target;
	var divs = document.querySelectorAll(".lineDiv");
	var arrows = document.querySelectorAll(".lineArrow");
	if(divs){
		for(var i=0;i<divs.length;i++){
			if(div.getAttribute("name") == divs[i].getAttribute("name")){
				divs[i].classList.add("lineDiv_hover");
			}
		}
	}
	if(arrows){
		for(var i=0;i<arrows.length;i++){
			if(div.getAttribute("name") == arrows[i].getAttribute("name")){
				arrows[i].classList.add("lineDiv_hover");
			}
		}
	}
	this.drawNode();
}

MM.Associate.prototype.outAssociate = function(e) {
	var divs = document.querySelectorAll(".lineDiv");
	var arrows = document.querySelectorAll(".lineArow");
	if(divs){
		for(var i=0;i<divs.length;i++){
			divs[i].classList.remove("lineDiv_hover");
		}
	}
	if(arrows){
		for(var i=0;i<arrows.length;i++){
			arrows[i].classList.remove("lineDiv_hover");
		}
	}
	this.hideNode();
	
}

MM.Associate.prototype.modifySide = function(e){
	var fromDiv = document.querySelector("#associateFrom");
	var toDiv = document.querySelector("#associateTo");
	var port = document.querySelector("#port");
	port.addEventListener("mousemove",this);
	port.addEventListener("mouseup",this);
	if(e.target == fromDiv){
		fromDiv.addEventListener("mousemove",this);
		fromDiv.addEventListener("mouseup",this);
		fromDiv.classList.add("nodeCurrent");
	}
	if(e.target == toDiv){
		toDiv.addEventListener("mousemove",this);
		toDiv.addEventListener("mouseup",this);
		toDiv.classList.add("nodeCurrent");
	}
	var associate = this.getSelectAssociate();
	if(associate){
		var divs = document.querySelectorAll(".lineDiv");
		if(divs){
			for(var i=0;i<divs.length;i++){
				divs[i].removeEventListener("blur",this);
			}
		} 
	}
}

MM.Associate.prototype.endSide = function(e){
	var div = document.querySelector(".nodeCurrent");
	var divId = div.getAttribute("id");
	var port = document.querySelector("#port");	
	var item = MM.App.map.getItemFor(e.target);
	var side =null;
	var map = MM.App.map;
	var associates = map.getAssociate();
	var associate = null
	for(var i=0;i<associates.length;i++){
		if(associates[i].getSelect()){
			associate = associates[i];
		}
	}
	if(item && !item.isRoot()){
		var root = item.getMap();
		var x = root._root.getDOM().node.offsetLeft;
		var y = root._root.getDOM().node.offsetTop;
		var dom = item.getDOM();
		var tempX = x + dom.node.offsetLeft;
		var tempY = y + dom.node.offsetTop;
		if(e.pageX <= tempX + dom.content.offsetWidth/4){
			side ="left";
		}else if(e.pageX > tempX + dom.content.offsetWidth*3/4){
			side ="right";
		}else {
			if(e.pageY <= tempY + dom.content.offsetHeight/2){
				side = "up";
			}else{
				side = "down";
			}
		}
	}
		
	if(side && associate){
		if(divId == "associateFrom"){
			if(side != associate.getFromSide()){
				associate._fromSide = side;
			}
			if(associate.getFrom()._id != item._id){
				associate._from = item;
			}
		}else if(divId == "associateTo"){
			if(side != associate.getToSide()){
				associate._toSide = side;
			}
			if(associate.getTo()._id != item._id){
				associate._to = item;
			}
		}
		associate.setFinish(true);
		this.update(associate);
		MM.App.associate= false;
	}
	var associate = this.getSelectAssociate();
	if(associate){
		var divs = document.querySelectorAll(".lineDiv");
		if(divs){
			divs[0].click();
			divs[0].focus();
			for(var i=0;i<divs.length;i++){
				divs[i].addEventListener("blur",this);
			}
		} 
	}
	div.removeEventListener("mousemove",this);
	div.removeEventListener("mouseup",this);
	div.classList.remove("nodeCurrent");
	port.removeEventListener("mousemove",this);
	port.removeEventListener("mouseup",this);
	this.drawNode();
	
	//this.select();
	
}
MM.Associate.prototype.moveSide = function(e){
	var div = document.querySelector(".nodeCurrent");
	div.style.left = e.pageX +"px";
	div.style.top = e.pageY +"px";
}

MM.Associate.prototype.handleEvent = function(e) {
	switch (e.type) {
		case "mousemove":
			var dom = document.querySelector(".nodeCurrent");
			if(this.getFinish()){
				this.startAssociate(e);
			}else if(dom){
				this.moveSide(e);
			}else{
				this.stretchAssociate(e);
			}
		break;
		
		case "blur":
			this.unSelectAssociate(e);
		break;

		case "click":
			var item = MM.App.map.getItemFor(e.target);
			if(item && !item.isRoot()){
				this.stopAssociate(e);
			}else if(e.target.classList.contains("lineDiv")){
				this.selectAssociate(e);
			}
			
		break;
		
		case "mousedown":
			var div = e.target;
			if(div.getAttribute("id") == "associateFrom" || div.getAttribute("id") == "associateTo"){
				this.modifySide(e);
			}else{
				
				this.modifyAssociate(e);
			}
		break;
		
		case "mouseup":
			var dom = document.querySelector(".nodeCurrent");
			if(dom){
				this.endSide(e);
			}else{
				this.finishAssociate(e);
			}
		break;
		
		case "mouseover":
			this.editAssociate(e);
		break;
		
		case "mouseout":
			this.outAssociate(e);
		break;
	}
}
/* 根据item和方向计算其起点或终点的坐标 */
MM.Associate.prototype.computerPosition = function(item,direction){
	var dom = item.getDOM();
	var position = item.getPosition();
	var newPosition = new Object();
	if(direction == "up"){
		newPosition.X = position.X + dom.content.offsetWidth/2;
		newPosition.Y = position.Y - dom.content.offsetHeight;
		return newPosition;
	}
	if(direction == "down"){
		newPosition.X = position.X + dom.content.offsetWidth/2;
		newPosition.Y = position.Y ;
		return newPosition;
	}
	if(direction == "left"){
		newPosition.X = position.X;
		newPosition.Y = position.Y - dom.content.offsetHeight/2;
		return newPosition;
	}		
	if(direction == "right"){
		newPosition.X = position.X + dom.content.offsetWidth;
		newPosition.Y = position.Y - dom.content.offsetHeight/2;
		return newPosition;
	}
}

MM.Associate.prototype.drawNode = function(){
	var fromDiv = document.querySelector("#associateFrom");
	var toDiv = document.querySelector("#associateTo");
	var fromPos = null;
	var toPos = null;
	if(this.getFrom()){
		fromPos = this.computerPosition(this.getFrom(), this.getFromSide());
		fromDiv.style.left= fromPos.X-5+"px";
		fromDiv.style.top = fromPos.Y-5 +"px";
		fromDiv.style.display="";
	}
	if(this.getTo()){
		toPos = this.computerPosition(this.getTo(), this.getToSide());
		toDiv.style.left=toPos.X-5+"px";
		toDiv.style.top = toPos.Y-5 +"px";
		toDiv.style.display="";
	}
	fromDiv.addEventListener("mousedown",this);
	toDiv.addEventListener("mousedown",this);
}

MM.Associate.prototype.hideNode = function(){
	var fromDiv = document.querySelector("#associateFrom");
	var toDiv = document.querySelector("#associateTo");
	fromDiv.style.display="none";
	toDiv.style.display="none";
}

/* 根据坐标点的位置来画线条 */
MM.Associate.prototype.drawConnector = function(){
	var points = this.getPositionPoints();
	var port = document.querySelector("#associatePort");
	var arrow = document.createElement("img");
	arrow.setAttribute("src","../icons/arrow.png");
	arrow.setAttribute("name",this._id );
	arrow.setAttribute("tabIndex", -1);
	arrow.classList.add("lineArrow");
	for(var i=0;i<points.length-1;i++){
		var div = document.createElement("div");
		div.classList.add("lineDiv");
		div.setAttribute("name",this._id );
		if(this.getTo()){
			div.setAttribute("num", i);
			div.setAttribute("tabIndex", -1);
			div.addEventListener("mouseover",this);
			div.addEventListener("mouseout",this);
			div.addEventListener("mouseup",this);
			div.addEventListener("blur",this);
			div.addEventListener("click",this);
			if(this.getToSide() == "up"){
				arrow.classList.add("arrow_up");
				arrow.style.left = points[points.length-1].X-7 +"px";
				arrow.style.top = points[points.length-1].Y-12 +"px";
			}
			if(this.getToSide() == "down"){
				arrow.classList.add("arrow_down");
				arrow.style.left = points[points.length-1].X-7 +"px";
				arrow.style.top = points[points.length-1].Y-3 +"px";
			}
			if(this.getToSide() == "left"){
				arrow.classList.add("arrow_left");
				arrow.style.left = points[points.length-1].X-12 +"px";
				arrow.style.top = points[points.length-1].Y-7 +"px";
			}
			if(this.getToSide() == "right"){
				arrow.classList.add("arrow_right");
				arrow.style.left = points[points.length-1].X-4 +"px";
				arrow.style.top = points[points.length-1].Y-7 +"px";
			}
		}else{
			arrow.classList.add("arrow_down");
			arrow.style.left = points[points.length-1].X-7 +"px";
			arrow.style.top = points[points.length-1].Y-3 +"px";
		}

		if(points[i].X == points[i+1].X){
			div.style.width = 0+"px";
			div.style.left = points[i].X + "px";
			if(points[i].Y <= points[i+1].Y){
				div.style.top = points[i].Y +"px";
				div.style.height = points[i+1].Y - points[i].Y +"px";
			}else{
				div.style.top = points[i+1].Y +"px";
				div.style.height = points[i].Y - points[i+1].Y +"px";
			}
		}else if(points[i].Y == points[i+1].Y){
			div.style.height = 0+"px";
			div.style.top = points[i].Y + "px";
			if(points[i].X <= points[i+1].X){
				div.style.left = points[i].X +"px";
				div.style.width = points[i+1].X - points[i].X +"px";
			}else{
				div.style.left = points[i+1].X +"px";
				div.style.width = points[i].X - points[i+1].X +"px";
			}
		}
		port.appendChild(div);
	}
	if(points.length){
		port.appendChild(arrow);
	}
	
}
/*计算移动后的坐标点*/
MM.Associate.prototype.movePosition = function(){
	if(this.getFrom() && this.getTo()){
		var positionPoints = this.getPositionPoints();
		var fromPos = this.computerPosition(this.getFrom(), this.getFromSide());
		var toPos = this.computerPosition(this.getTo(), this.getToSide());
		var tempX = fromPos.X - positionPoints[0].X;
		var tempY = fromPos.Y - positionPoints[0].Y;
		for(var i=0;i<positionPoints.length;i++){
			positionPoints[i].X += tempX;
			positionPoints[i].Y += tempY;
		}
	}
}
/*计算缩放后的坐标点*/
MM.Associate.prototype.adjustPosition = function(){
	if(this.getFrom() && this.getTo()){
		var positionPoints = this.getPositionPoints();
		var fromPos = this.computerPosition(this.getFrom(), this.getFromSide());
		var toPos = this.computerPosition(this.getTo(), this.getToSide());
		if(1 < positionPoints.length){
			var len = positionPoints.length;
			var tempX = toPos.X-fromPos.X;
			var tempY = toPos.Y-fromPos.Y;
			var rateX = 1;
			var rateY = 1;
			positionPoints = this.getPositionPoints();
			if(positionPoints[len-1].X != positionPoints[0].X){
				var rateX = tempX/(positionPoints[len-1].X-positionPoints[0].X);
			}
			if(positionPoints[len-1].Y != positionPoints[0].Y){
				var rateY = tempY/(positionPoints[len-1].Y-positionPoints[0].Y);
			}
			/*if(rateX == 1){
				rateX = rateY;
			}
			if(rateY == 1){
				rateY = rateX;
			}*/
			if(tempX != (positionPoints[len-1].X - positionPoints[0].X) || tempY != (positionPoints[len-1].Y - positionPoints[0].Y)){
				for(var i=1;i<len;i++){
					if(positionPoints[i].X != positionPoints[0].X){
						positionPoints[i].X = (positionPoints[i].X-positionPoints[0].X)*(rateX-1) + positionPoints[i].X;
					}
					if(positionPoints[i].Y != positionPoints[0].Y){
						positionPoints[i].Y = (positionPoints[i].Y-positionPoints[0].Y)*(rateY-1) + positionPoints[i].Y;
					}
				}
			}
				
			
		}		
	}
}
/* 根据起点和终点的坐标计算默认的转折点坐标 */
MM.Associate.prototype.computerConnectorPoint = function(from,to){
	var pointArray = [];
	var point = null;
	var newX = from.X;
	var newY = from.Y;
	var insert = function(){
		point = new Object();
		point.X = newX;
		point.Y = newY;
		pointArray.push(point);
	}
	/* 第一个坐标点为起点 */
	insert();
	
	/* 计算中间折线坐标点 */
	if(from.side == "up"){
		newY = newY - from.width;
		insert();
		
		if(to.side == "right"){
			newX = to.X + to.width;
			insert();
			
			newY = to.Y;
			insert();
		}else if(to.side == "left"){
			newX = to.X - to.width;
			insert();
			
			newY = to.Y;
			insert();			
		}else if(to.side == "up"){
			var tempX = to.X - newX;
			if(tempX < -20 || tempX > 20){
				newX = newX + tempX/2;
			}else{
				if(tempX <= 0){
					newX = newX - 10;
				}else{
					newX = newX + 10;
				}
			}
			insert();
			
			newY = to.Y - to.width;
			insert();
			
			newX = to.X;
			insert();
			
		}else if(to.side == "down"){
			var tempX = to.X - newX;
			if(tempX < -20 || tempX > 20){
				newX = newX + tempX/2;
			}else{
				if(tempX <= 0){
					newX = newX - 10;
				}else{
					newX = newX + 10;
				}
			}
			insert();
			
			newY = to.Y + to.width;
			insert();
			
			newX = to.X;
			insert();
		}
	}else if(from.side == "down"){
		newY = newY + from.width;
		insert();
		
		if(to.side == "right"){
			newX = to.X + to.width;
			insert();
			
			newY = to.Y;
			insert();
		}else if(to.side == "left"){
			newX = to.X - to.width;
			insert();
			
			newY = to.Y;
			insert();			
		}else if(to.side == "up"){
			var tempX = to.X - newX;
			if(tempX < -20 || tempX > 20){
				newX = newX + tempX/2;
			}else{
				if(tempX <= 0){
					newX = newX - 10;
				}else{
					newX = newX + 10;
				}
			}
			insert();
			
			newY = to.Y - to.width;
			insert();
			
			newX = to.X;
			insert();
			
		}else if(to.side == "down"){
			var tempX = to.X - newX;
			if(tempX < -20 || tempX > 20){
				newX = newX + tempX/2;
			}else{
				if(tempX <= 0){
					newX = newX - 10;
				}else{
					newX = newX + 10;
				}
			}
			insert();
			
			newY = to.Y + to.width;
			insert();
			
			newX = to.X;
			insert();
		}
	}else if(from.side == "right"){
		newX = newX + from.width;
		insert();
		
		if(to.side == "up"){
			newY = to.Y - to.width;
			insert();
			
			newX = to.X;
			insert();
		}else if(to.side == "down"){
			newY = to.Y + to.width;
			insert();
			
			newX = to.X;
			insert();			
		}else if(to.side == "left"){
			var tempY = to.Y - newY;
			if(tempY < -20 || tempY > 20){
				newY = newY + tempY/2;
			}else{
				if(tempY <= 0){
					newY = newY - 10;
				}else{
					newY = newY + 10;
				}
			}
			insert();
			
			newX = to.X - to.width;
			insert();
			
			newY = to.Y;
			insert();
			
		}else if(to.side == "right"){
			var tempY = to.Y - newY;
			if(tempY < -20 || tempY > 20){
				newY = newY + tempY/2;
			}else{
				if(tempY <= 0){
					newY = newY - 10;
				}else{
					newY = newY + 10;
				}
			}
			insert();
			
			newX = to.X + to.width;
			insert();
			
			newY = to.Y;
			insert();
		}
	}else if(from.side == "left"){
		newX = newX - from.width;
		insert();
		
		if(to.side == "up"){
			newY = to.Y - to.width;
			insert();
			
			newX = to.X;
			insert();
		}else if(to.side == "down"){
			newY = to.Y + to.width;
			insert();
			
			newX = to.X;
			insert();			
		}else if(to.side == "left"){
			var tempY = to.Y - newY;
			if(tempY < -20 || tempY > 20){
				newY = newY + tempY/2;
			}else{
				if(tempY <= 0){
					newY = newY - 10;
				}else{
					newY = newY + 10;
				}
			}
			insert();
			
			newX = to.X - to.width;
			insert();
			
			newY = to.Y;
			insert();
			
		}else if(to.side == "right"){
			var tempY = to.Y - newY;
			if(tempY < -20 || tempY > 20){
				newY = newY + tempY/2;
			}else{
				if(tempY <= 0){
					newY = newY - 10;
				}else{
					newY = newY + 10;
				}
			}
			insert();
			
			newX = to.X + to.width;
			insert();
			
			newY = to.Y;
			insert();
		}
	}
	newX = to.X;
	newY = to.Y;
	insert();
	return pointArray;
}
function addRow(evaluates){
	var item = MM.App.current;
	var table = document.querySelector("#evaluateTable");
	if(evaluates){
		for(var tableIndex=0;tableIndex<evaluates.length;tableIndex++){
			var tr = table.insertRow();
			tr.insertCell().innerHTML=''+(tableIndex+1)+'';
			tr.insertCell().innerHTML='<input type="text" style="width:60px" name="mangeIndex['+(tableIndex+1)+'].name" value="'+evaluates[tableIndex].getName()+'"/>';
			tr.insertCell().innerHTML='<input type="text" style="width:60px" name="mangeIndex['+(tableIndex+1)+'].unit" value="'+evaluates[tableIndex].getUnit()+'"/>';
			tr.insertCell().innerHTML='<input type="text" style="width:60px" name="mangeIndex['+(tableIndex+1)+'].type" value="'+evaluates[tableIndex].getType()+'"/>';
			tr.insertCell().innerHTML='<input type="button" value="删除" onclick="delRow(this)"/>';
		}
	}else{
		var tableIndex=table.rows.length;
		var tr = table.insertRow();
		tr.insertCell().innerHTML=''+tableIndex+'';
		tr.insertCell().innerHTML='<input type="text" style="width:60px" name="mangeIndex['+tableIndex+'].name"/>';
		tr.insertCell().innerHTML='<input type="text" style="width:60px" name="mangeIndex['+tableIndex+'].unit"/>';
		tr.insertCell().innerHTML='<input type="text" style="width:60px" name="mangeIndex['+tableIndex+'].type"/>';
		tr.insertCell().innerHTML='<input type="button" value="删除" onclick="delRow(this)"/>';
	}
}

function delRow(e){
	var div = document.getElementById("evaluateIndex");
	var table = document.querySelector("#evaluateTable");
	var item = MM.App.current;
	var index = e.parentElement.parentElement.rowIndex;
	if ((index-1) < item.getEvaluate().length){
		item.getEvaluate().splice(index-1,1);
	}
	table.deleteRow(index);
	for(var tableIndex=1;tableIndex<table.rows.length;){
		table.rows[tableIndex].cells[0].innerHTML=''+tableIndex+'';
		table.rows[tableIndex].cells[1].children[0].name='mangeIndex['+tableIndex+'].name';
		table.rows[tableIndex].cells[2].children[0].name='mangeIndex['+tableIndex+'].unit';
		table.rows[tableIndex].cells[3].children[0].name='mangeIndex['+tableIndex+'].type';
		tableIndex++;
	}
	div.focus();
}