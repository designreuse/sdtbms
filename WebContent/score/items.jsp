<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ss" uri="/WEB-INF/StripesSecurityManager.tld" %>
<stripes:layout-render name="../default.jsp">
	
    <stripes:layout-component name="contents">
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/custom/scoreListBuilder.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/score.js"></script>
    <link href="${pageContext.request.contextPath}/css/custom_general.css" rel="stylesheet" media="all" />
    <link href="${pageContext.request.contextPath}/css/custom/scoreListBuilder.css" rel="stylesheet" title="style"/>
    <style type="text/css">
    	.readonly{
    		background-color:#CCCCBB;
    	}
    	.button {
    font: bold 11px Arial;
    text-decoration: none;
    background-color: #EEEEEE;
    color: #333333;
    padding: 2px 6px 2px 6px;
    border-top: 1px solid #CCCCCC;
    border-right: 1px solid #333333;
    border-bottom: 1px solid #333333;
    border-left: 1px solid #CCCCCC;
   }
    .divtitle { width:550px; height:25px; border:1px solid #000; margin:0px;}
	.divtitle div { float:left;}
   
	.tiaoli{ font-size:18px; font-weight:900; width:240px; height:22px; text-align:center; border-right:1px solid #000; margin-top:3px; }
	.fenzhi{ font-size:18px; font-weight:900; height:22px; width:55px;text-align:center; border-right:1px solid #000;  margin-top:3px;}
	.zhushi{ font-size:18px; font-weight:900; height:22px; width:180px;text-align:center; margin-top:3px; }
	.divbobycontent { clear:both; width:550px; height:43px; border:1px solid #000; }
	.divbobycontent div { float:left;}

	.tiaoli2 { font-size:11px; width:240px; height:40px ;*height:38px; text-align:center;vertical-align; border-right:1px solid #000;padding-top:4px;*padding-top:6px; }
	.fenzhi2 { font-size:11px; width:55px; height:43px; text-align:center; border-right:1px solid #000;}
	.zhushi2 { font-size:11px; width:253px; height:43px;}
	
	.inputscore { height:34px; width:52px; border:none; text-align:center; margin-top:3px; background-color:#FFF}
	.inputscore2 { height:34px; width:52px; border:none; text-align:center; margin-top:3px; background-color:#f2f2f2}
	.texrarearemark { border:none; width:248px; *width:251px; height:38px; *height:41px;}
	
    </style>
    <script type="text/javascript">
   
	
	     function openSelectEmpWindow(names,workerids,extras,multi){
    		var link="";
        	if(multi == true)
        		link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action?score=yes&eleIdOne="+names+"&eleIdTwo="+workerids+"&extra="+extras+"&getEmployeeScoreSelectionList=";
        	else
        		link = "${pageContext.request.contextPath}/actionbean/EmployeeSelector.action?score=yes&eleIdOne="+names+"&eleIdTwo="+workerids+"&extra="+extras;
			window.open(link,"_blank","fullscreen=no,scrollbars=1,width=600,height=600");
        }
        $(document).ready(function(){
        	
        	$('#selectAll').click(function(){
				if(this.checked){
					$('#dataForm :checkbox').each(function(){
						this.checked = true;
					});
				}else{
					$('#dataForm :checkbox').each(function(){
						this.checked = false;
					});
				}
			});

			$('#givescores').click(function(){
				if(!isSelection())
					return;
				if(!isScorerSelected())
					return;
				var checkedBox = $('#dataForm input:checkbox:checked');

				if(checkZeroScores(checkedBox)){
					$('#setScoreDialog').dialog('open');
				}else{
					giveScores();
				}
			});

			
			function giveScores(){
				var ele = $('#givescores');
				$(ele).hide();					
				var url = $(ele).children().first().val();
				var serialize  = $('#dataForm').serialize();
// 				alert(url);
				
				$.ajax({
					url:url,
					type:"post",
					dataType:"text",
					data:serialize,
					success:function(response){
						var jobj = $.parseJSON(response);
						if(jobj.result == "1"){
							clearCheckBoxesAndReceivers();
						}
						alert(jobj.msg);
						$('#givescores').show();
					}
				});
				setTimeout(function(){
					$('#givescores').show();
				},3000);
			}

			
			$("#setScoreDialog").dialog({
				autoOpen: false,
				bgiframe: true,
				resizable: true,
// 				height:100,
				width:580,
				modal: true,
				overlay: {
					backgroundColor: '#000',
					opacity: 0.5
				},
				buttons: {
					'确定':function(){
						var valOk = true;
						$(this).find(":input[type=text]").each(function(){
							var temScore = $.trim($(this).val());
							//alert(temScore);
							if(temScore == ""){
								valOk = false;
								alert("分值不能为空！");
							}else if(parseFloat(temScore) <= 0 || parseFloat(temScore) >= 1000){
								valOk = false;
								alert("分值不能小于或等于0,或值过大");
							}else if($(this).next().val() == "绩效"){
// 								alert("绩效分");
								var s = parseFloat(temScore);
								if(s > 100.0){
									valOk = false;
									alert("分值不能大于100");
								}
							}
						});
						if(!valOk)
							return false;
						
						$(this).find("div.fenzhi2>input").each(function(){
							var score  = $(this).val();
							var name = $(this).attr('name');
							//alert(score + "   " + name);
							$('#dataForm input[name="'+name+'"]').val(score);	
						});


												
						//添加 积分 注释
						$(this).find("div.zhushi2>textarea").each(function(){
							var recordRemark  = $(this).val();
							var recordRemarkname = $(this).attr('name');
							//alert(recordRemark+"  " + recordRemarkname);						
							$('#dataForm input[name="'+recordRemarkname+'"]').val(recordRemark);
		
						});
						
 						giveScores();
						$(this).dialog('close');
					},
					'取消': function() {
						$(this).dialog('close');
					}
				}
			});
        });
        function clearCheckBoxesAndReceivers(){
			$("#dataForm input:checked").each(function(){
				$(this).click();
			});
			$('#employeenamefromid2').val("");
			$('#checkWorkerId2').val("");
		}
        function isSelection(){
			var count = $('#dataForm input:checkbox:checked').length;
			if(count <= 0){
				alert("未选上任何项目");
				return false;
			}
			return true;
		}
		function isScorerSelected(){
			var name = $('#employeenamefromid2').val();
			var wids = $('#checkWorkerId2').val();
			if(name == "" || wids == ""){
				alert("请选择受分人");
				return false;
			}
			return true;
		}
		function checkZeroScores(checked){
			var htmlstr = "";
			$(checked).each(function(){
				var references = $(this).parent().parent().children(":nth-child(2)").html();
				var types = $(this).parent().parent().children(":nth-child(3)").html();
//				var words = $(this).parent().parent().children(":nth-child(4)").html().split("<input");
				var words = $(this).parent().parent().children(":nth-child(4)").html().split("<");
// 				alert($(this).parent().parent().children(":nth-child(4)").html());
// 				alert(words[0]);
				var item = $(this).parent().parent().children(":nth-child(5)").html();
// 				var scoreValue = $(this).parent().parent().children(":nth-child(4)").children().first().val();
				var scoreValue = words[0];
				var valueName = $(this).parent().parent().children(":nth-child(4)").children().first().attr('name');
				var valueName2 = $(this).parent().parent().children(":nth-child(9)").children().first().attr('name');
				if(parseFloat(scoreValue) == 0){
//					htmlstr += "<li>条例:"+item+"<br/>分值:<input type='text' name='"+valueName+"'/><input type='hidden' value='"+types+"'/></li><br/>";
//					htmlstr += "<li>条例:"+item+"<br/>分值:<input type='text' name='"+valueName+"'/><input type='hidden' value='"+types+"'/>&nbsp;&nbsp;注释:<input type='text' name='"+valueName2+"' style='width:180px;' /></li><br/>";
//					htmlstr += "<li><div>条例:"+item+"</div><br/>分值:<input type='text' name='"+valueName+"'/><input type='hidden' value='"+types+"'/>&nbsp;&nbsp;注释:<textarea name='"+valueName2+"' cols='20' rows='1' style='margin-top:3px;' /></li><br/>";
                    htmlstr += "<div class='divbobycontent'><div class='tiaoli2'>"+item+"</div><div class='fenzhi2'><input type='text' class='inputscore' name='"+valueName+"' /><input type='hidden' value='"+types+"'/></div><div class='zhushi2'><textarea class='texrarearemark' name='"+valueName2+"'/></div></div>";
				}
				else{
//					htmlstr += "<li>条例:"+item+"<br/>注释:<input type='text' name='"+valueName2+"' style='width:180px;' /></li><br/>";
//					htmlstr += "<li><div>条例:"+item+"</div><br/>注释:<textarea name='"+valueName2+"' cols='20' rows='1' style='margin-top:3px;' /></li><br/>";
					htmlstr += "<div class='divbobycontent'><div class='tiaoli2'>"+item+"</div><div class='fenzhi2'><input type='text' class='inputscore2' name='"+valueName+"' value='"+scoreValue+"' readonly='true' /><input type='hidden' value='"+types+"'/></div><div class='zhushi2'><textarea class='texrarearemark' name='"+valueName2+"'  /></div></div>";
					}
				
			});
			if(htmlstr != ""){
				//$('#setScoreDialog').html("<ul>"+htmlstr+"</ul>");
				$('#setScoreDialog').html("<div class='divtitle'><div class='tiaoli'>条例</div><div class='fenzhi'>分值</div><div class='zhushi'>注释</div></div><div class='divboby'>"+htmlstr+"</div>");
				return true;
			}else{
				return false;
			}
		}
    </script>
    
		<div id="sub-nav"><div class="page-title">
			<h1>积分主页</h1>
		</div></div>
		
		<div id="page-layout"><div id="page-content">
			<div id="page-content-wrapper">
			
			
			
			<!-- Side bar ****************************************************************** -->
			<jsp:include page="/score/sidebar.jsp" />
			
			
			
		<!-- Side bar ****************************************************************** -->
				<!-- Start of main content -->
				<div id="rightcontent">
				<!-- 新建条例  Dialog-->
				<div id="items_top_button_bar" style="height:40px;">&nbsp;  
				<a class="btn ui-state-default ui-corner-all" href="javascript:location.reload();">
					刷新
				</a>
				
				<ss:secure roles="score_items_create">
				<a id="btn_new_item_link" class="btn ui-state-default ui-corner-all" href="#">
						<span class="ui-icon ui-icon-person"></span>
						新建条例
				</a>
				
				<div id="btn_new_item_dialog" title="新建条例">
					<stripes:form id="form_new_item" beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
						<div class='hastable'>
						<table>
								<tr>
									<td>编号:</td><td><stripes:text name="scoretype.reference" class="required"/></td>
								</tr>
								<tr>
									<td>类型:</td><td><stripes:select name="scoretype.type"  class="required"><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option><stripes:option value="2">绩效分</stripes:option><stripes:option value="3">项目分</stripes:option></stripes:select></td>
								</tr>
								<tr>
									<td>注释:</td><td><stripes:textarea name="scoretype.description"  class="required"/></td>
								</tr>
								<tr>	
									<td>分值:</td><td><stripes:text name="scoretype.score"  class="required" formatType="number" formatPattern="integer"></stripes:text></td>
								</tr>
								<tr>	
									<td>周期:</td><td><stripes:text name="scoretype.period"  class="required"></stripes:text></td>
								</tr>
								<tr>	
									<td>考核部门:</td><td><stripes:text name="scoretype.examine"  class="required"></stripes:text></td>
								</tr>
								<tr>	
									<td>评分对象:</td><td><stripes:text name="scoretype.scoreobject"  class="required"></stripes:text></td>
								</tr>
								<tr>	
									<td>备注:</td>
									<td>
										<stripes:textarea name="scoretype.remark" cols="40" rows="7"/>
									</td>
								</tr>
						</table>
						</div>
					</stripes:form>
				</div>
				</ss:secure>
				</div>
				<hr/>
				
				<!-- Show items and pages options -->
			<div>
				<div class="hastable">
						<div class="inner-page-title">
						条例信息
						</div>
				<stripes:form id='dataForm' beanclass="com.bus.stripes.actionbean.score.ScoreitemsActionBean">
				<table>
					<thead>
					
						<tr>
							<td colspan=9 style="text-align:left">
								<stripes:submit name="prevpage" value="上页"/> 页码: <stripes:text name="pagenum"/> /<stripes:label name="${actionBean.totalcount}"/>  <stripes:submit name="nextpage" value="下页"/>
								显示数量:<stripes:text name="lotsize"/>
								<stripes:submit id='filter' name="filter" value="筛选"/>
							</td>
						</tr>
						<tr>
							<td colspan=9 style="text-align:left">
								<ss:secure roles="score_items_givescore">
								<div>
									<Label class='selector'>给分人:</Label>
									<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;名称:<stripes:text readonly="true" name="employee.fullname" style="background-color:#CCCCBB;"  id="employeenamefromid1"/>工号:<stripes:text name="employee.workerid" readonly="true" id="checkWorkerId1" style="background-color:#CCCCBB;"/>
								</div>
								<div>
									<Label class='selector'>受分人:</Label>
									<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;名称:<stripes:text style="background-color:#CCCCBB;width:50%;" readonly="readonly" name="receivers" id="employeenamefromid2"/>
									<br/>
									&nbsp;&nbsp;&nbsp;&nbsp;工号:<stripes:text name="receiverWorkerids" id="checkWorkerId2"/>
<%-- 									<a href="javascript:void;" id="checkWorkerId">(查)</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?checkworkerid="/>| --%>
<%-- 									<a href="javascript:void;" id="getNameById2">获取</a><input type="hidden" value="${pageContext.request.contextPath}/actionbean/Employee.action?getnamebyid="/>| --%>
									<input type="hidden" value="" id="extra2"/><a href="javascript:void;" onclick="openSelectEmpWindow('employeenamefromid2','checkWorkerId2','extra2',true)">从列表选择</a>
								</div>
<!-- 								<div> -->
<%-- 									<Label class='selector'>自拟定分值:</Label><stripes:text name="score"/><span style="font-size:9pt;color:#CCCCCC;">(0为默认)</span> --%>
<!-- 								</div> -->
								<div>
									<Label class='selector'>日期:</Label><stripes:text name="scoredate" formatPattern="yyyy-MM-dd" class="datepickerClass"/>
								</div>
								<div>
									<a href="javascript:void;" id="givescores" class="button">
									打分
									<input type="hidden" value="${pageContext.request.contextPath}/actionbean/Scoreitems.action?givescores="/>
									</a>
								</div>
								</ss:secure>
								
							</td>
						</tr>
						<tr>
							<td colspan=9 style="text-align:left">
								<Label class='selector'>关键词:</Label><stripes:text name="selector.itemWords"/>
								<br/>
								<Label class='selector'>编号:</Label><stripes:text name="selector.reference"/>
								<Label class='selector'>类型:</Label><stripes:select name="selector.type"><stripes:option value="">不限</stripes:option><stripes:option value="0">临时分</stripes:option><stripes:option value="1">固定分</stripes:option><stripes:option value="2">绩效分</stripes:option><stripes:option value="3">项目分</stripes:option></stripes:select>
								<Label class='selector'>条例单:</Label><stripes:select id='itemlist' name="selector.itemlist"><stripes:option value=""></stripes:option>不限<stripes:options-collection collection="${actionBean.sheetList}" label="name" value="id"/></stripes:select>
								<stripes:submit id='filter' name="filter" value="筛选"/>
								<br/>
								<ss:secure roles="score_items_edit">
									<stripes:submit name="deletescoretype" value="删除"/>
								</ss:secure>
							<ss:secure roles="score_sheet_add_st">
									<stripes:submit name="assignToScoreSheet" value="添加到积分表单"/>
								</ss:secure>
									
							</td>
						</tr>
						<tr>
							<th>全选<input type="checkbox" id="selectAll"/></th>
							<th>编号</th>
							<th>类型</th>
							<th>分值</th>
							<th style="width:300px !important;">条例</th>
							<th>周期</th>
							<th>奖分对象</th>
							<th>考核单位</th>
							<th style="width:300px !important;">备注</th>
						</tr>
					</thead>
					<tbody>
					<c:set var="color" value="0" scope="page"/>
					<c:forEach items="${actionBean.scoretypes}" var="st" varStatus="loop">
						<c:choose>
							<c:when test="${color%2 == 0}">
								<tr>
							</c:when>
							<c:otherwise>
								<tr class="alt">
							</c:otherwise>
						</c:choose>
							<td>
								<input type="checkbox" name="selectedScoreTypes[${color}].id" value="${st.id}"/>
								<ss:secure roles="score_items_edit">
										<a target="_blank" href="${pageContext.request.contextPath}/actionbean/Scoreitems.action?editscoretype=&targetId=${st.id}">修改</a>
								</ss:secure>
							</td>
							<td>${st.reference}</td>
							<td>${st.typestr}</td>
							<td>${st.score}<input type="hidden" name="selectedScores[${color}]" value="${st.score}"/></td>
							<td>${st.description}</td>
							<td>${st.period}</td>
							<td>${st.scoreobject}</td>
							<td>${st.examine}</td>
							<td>${st.remark}<input type="hidden" name="recordRemark[${color}].remark" value="" /></td>
		                    							
						</tr>
						<c:set var="color" value="${color + 1}" scope="page"/>
					</c:forEach>
					</tbody>
				</table>
				</stripes:form>
				</div>
			</div>
				
				
				</div>
				<div id="setScoreDialog">
					
				</div>
				<div id="selectEmpDialog">
				</div>
				<!-- End of main content -->
			</div>
			<div class="clear"></div>
		</div>
	</div>
    </stripes:layout-component>
</stripes:layout-render>