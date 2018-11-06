/**
 * 
 */

		function indexOf(arr, val) {
			for (var i = 0; i < arr.length; i++) {
				if (arr[i] == val) {
					return i;
				}
			}
			return -1;
		};

		function nodeToJson(nodeJson, intfCode){
			var rs = {method : intfCode};
			loopJson(nodeJson, rs);
			
			function loopJson(json, aim){
				var attrName = json.attrName;
				var attrType = json.attrType;
				if(attrName && '' != attrName){//存在属性name
					if(!attrType || '' == attrType || 'false' == attrType){
						aim[attrName] = '';
					}else if('list' == attrType){
						var sub = {};
						aim[attrName] = [sub];
						loopJson(json.children[0], sub);
					}
				}else if(json.children && json.children.length>0){
					var subArr = json.children;
					for(var i=0; i<subArr.length; i++){
						loopJson(subArr[i], aim);
					}
				}
			}
			
			return rs;
		}
		
		function nodeToJson2(nodeJson){
			var rs = {method : '接口方法, 必须'};
			loopJson(nodeJson, rs);
			
			function loopJson(json, aim){
				var attrName = json.attrName;
				var attrType = json.attrType;
				if(attrName && '' != attrName){//存在属性name
					if(!attrType || '' == attrType || 'false' == attrType){
						aim[attrName] = (json.text+ ('false' == attrType ? '' : ', 必须'));
					}else if('list' == attrType){
						var sub = {};
						aim[attrName] = [sub];
						loopJson(json.children[0], sub);
					}
				}else if(json.children && json.children.length>0){
					var subArr = json.children;
					for(var i=0; i<subArr.length; i++){
						loopJson(subArr[i], aim);
					}
				}
			}
			
			return rs;
		}
		
		function getNodeDesc(nodeJson){
			var rs = {method : '接口方法, 必须'};
			loopJson(nodeJson);
			
			function loopJson(json){
				var attrName = json.attrName;
				var attrType = json.attrType;
				if(attrName && '' != attrName){//存在属性name
					if(!attrType || '' == attrType || 'false' == attrType){
						rs[attrName] = (json.text+ ('false' == attrType ? '' : ', 必须'));
					}
				}
				if(json.children && json.children.length>0){
					var subArr = json.children;
					for(var i=0; i<subArr.length; i++){
						loopJson(subArr[i]);
					}
				}
			}
			
			return rs;
		}
		
		function mathRand(){ 
			var Num=""; 
			for(var i=0;i<6;i++){ 
				Num+=Math.floor(Math.random()*10); 
			}
			return Num;
		} 
		
		function GridUtil(grid, ecolArr, datas, type) {
			var _this = this;
			//var prevId = 0;
			var curr_id;
			var isInArea = false;
			var editArr = new Array();
			var defData = clean($.extend(true, {}, datas.rows[0]));

			function clean(d){
				for(var p in d){
					d[p] = '';
				}
				return d;
			}
			
			_this.getId = function(pid) {
				var t = (new Date()).getTime();
				/*var chs = grid.treegrid("getChildren", pid);
				if(chs && chs.length>0){
					prevId = chs[chs.length-1].id
					prevId = Number(prevId.substring(pid.length, prevId.length));
				}
				if (prevId == 1000) {
					prevId = 0;
				}
				var seq = '';
				prevId += 1;
				if (prevId < 10) {
					seq = '00' + prevId;
				} else if (prevId >= 10 && prevId < 100) {
					seq = '0' + prevId;
				} else {
					seq = prevId;
				}*/
				return t + mathRand();
			}

			_this.edit = function(rowid) {
				if(type==1){
					grid.datagrid('beginEdit', rowid); //rowid为index
				}else{
					grid.treegrid('beginEdit', rowid); 
				}
				editArr.push(rowid);
			};
			
			_this.cancelEdit = function(rowid) {
				operation(rowid, 'cancelEdit');
			};

			_this.save = function(rowid) {
				operation(rowid, 'endEdit');
			};

			_this.getEditRows = function() {
				return editArr;
			};

			_this.addRow = function() {
				var id = _this.getId(curr_id);
				var temp = $.extend(true, {}, _this.getDefData());
				temp.id = id;
				if(type=='1'){
					id = Number(curr_id)+1;
					grid.datagrid('appendRow', temp);
				}else{
					temp.parentId = curr_id;
					grid.treegrid('append', {
						parent : curr_id,
						data : [ temp ]
					});
				}
				_this.edit(id);
				var tr = grid.find('[node-id="' + id + '"]');
				_this.init(tr);
			};

			_this.delRow = function() {
				if(type=='1'){
					if(grid.datagrid('getRows').length>1){
						grid.datagrid('deleteRow', curr_id);
						curr_id--;
					}
				}else{					
					grid.treegrid('remove', curr_id);
					curr_id = '';
				}
			};

			_this.getCurrId = function() {
				return curr_id;
			};
			
			_this.getDefData = function() {
				return defData;
			};
			
			var editBtns = $('#_edit_btns');
			editBtns.mouseover(function() {
				editBtns.show();
				isInArea = true;
			});
			editBtns.mouseout(function() {
				editBtns.hide();
				isInArea = false;
			});

			GridUtil.currTab;
			_this.init = function(data, tr) {
				var trs = (tr ? tr : grid.prev().find('.datagrid-row'));
				trs.mouseover(function(e) {
					var offset = $(this).offset();
					editBtns.show();
					var width = $(this).width();
					if (width > grid.prev().width()) {
						width = grid.prev().width();
					}
					editBtns.css({
						left : offset.left + width,
						top : offset.top
					});
					isInArea = true;
					if(type=='1'){
						curr_id = $(this).attr('datagrid-row-index');
					}else{
						curr_id = $(this).attr('node-id');
					}
					console.log("tr over curr_id="+curr_id)
					var pobj = $(this).parent();
					while (!pobj.hasClass('datagrid-body')) {
						pobj = pobj.parent();
					}
					GridUtil.currTab = pobj.parent().next().attr('id');
				});

				grid.parent().mouseout(function() {
					//当鼠标移出数据表格区域，启动定时器，当鼠标不在图标或数据区域时，定时器到时则执行，否则到时了也不执行
					isInArea = false;
					setTimeout(function() {
						if (!isInArea) {
							editBtns.hide();
							curr_id = '';
						}
					}, 2000);
				});
			};
			/**@type  */
			function operation(rowid, type) {
				if (rowid && rowid != undefined) {
					if(type=='1'){
						grid.datagrid(type, rowid);
					}else{	
						grid.treegrid(type, rowid);
					}
					var index = indexOf(editArr, rowid);
					editArr.splice(index, 1);
				} else {
					for (var i = 0; i < editArr.length; i++) {
						if(type=='1'){
							grid.datagrid(type, editArr[i]);
						}else{
							grid.treegrid(type, editArr[i]);
						}
					}
					editArr = new Array();
				}
			}

			if(type=='1'){
				grid.datagrid({
					columns : ecolArr,
					onDblClickRow : function(index, row) {
						_this.edit(index);
					},
					onLoadSuccess : function(data) {
						_this.init(data);
					}
				});
				grid.datagrid("loadData", datas);
			}else{
				grid.treegrid({
					columns : ecolArr,
					onDblClickRow : function(row) {
						_this.edit(row.id);
					},
					onLoadSuccess : function(row, data) {
						_this.init(data);
					}
				});
				grid.treegrid("loadData", datas);
			}
		}