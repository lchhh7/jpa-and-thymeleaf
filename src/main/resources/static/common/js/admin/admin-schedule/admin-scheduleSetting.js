function adminDetail(id){
          $.ajax({
            url: contextPath + "schedule/" + id +".do",
            type: "get",
          })
          .done(function(data) {
        	  var form = document.approveForm;
			  form.id.value = data.id;
			  form.memberId.value = data.memberName;
        	  form.type.value = data.typeName;
        	  form.startDt.value = Unix_timestamp(new Date(data.startDt));
        	  form.startTm.value = data.startTm.substr(0,2)+":"+data.startTm.substr(2,4);
        	  form.endDt.value = Unix_timestamp(new Date(data.endDt));
			  if(data.type == 5){
				form.endTm.value = data.endTm.substr(0,1);
			  }else{
        	  form.endTm.value = data.endTm.substr(0,2)+":"+data.endTm.substr(2,4);
              }
        	  form.title.value = data.title;
        	  form.content.value = data.content;
        	  openModal('detail-info-modal');
        		
        	  var tmp = data.status;
				if(tmp =='R'){
					document.getElementById('adminDeleteApproveButton').style.display = 'none';
					document.getElementById('adminDeleteNoneApproveButton').style.display = 'none';
					document.getElementById('adminScheduleApproveButton').style.display = 'inline-block';
					document.getElementById('adminScheduleNoneApproveButton').style.display = 'inline-block';
					
				}else if(tmp =='DR'){
					document.getElementById('adminScheduleApproveButton').style.display = 'none';
					document.getElementById('adminScheduleNoneApproveButton').style.display = 'none';
					document.getElementById('adminDeleteApproveButton').style.display = 'inline-block';
					document.getElementById('adminDeleteNoneApproveButton').style.display = 'inline-block';
				}else{
					document.getElementById('adminScheduleApproveButton').style.display = 'none';
					document.getElementById('adminScheduleNoneApproveButton').style.display = 'none';
					document.getElementById('adminDeleteApproveButton').style.display = 'none';
					document.getElementById('adminDeleteNoneApproveButton').style.display = 'none';
				}
          })
		}

function approve(status){
	var data ={
		id :  document.approveForm.id.value,
		status : status
	}
	
	$.ajax({
		type: "patch",
        url: contextPath + "planApprove/" + document.approveForm.id.value +".do",
		data : JSON.stringify(data),
		contentType: 'application/json; charset=utf-8'
      })
      .done(function(data) {
		alert(data);
		location.reload();
	})
	 .fail(function(data) {
		alert(data.responseText);
		return false;
	});
}

function deleteApprove(status){
	if(status == 'Y'){
		var data ={
			id :  document.approveForm.id.value,
			status : 'D',
			delDt : 1
		}
	}else if(status == 'N'){
		var data ={
			id :  document.approveForm.id.value,
			status : 'Y'
		}
	}
	$.ajax({
		type: "patch",
        url: contextPath + "deleteApprove/" + document.approveForm.id.value +".do",
		data : JSON.stringify(data),
		contentType: 'application/json; charset=utf-8'
      })
      .done(function(data) {
		alert(data);
		location.reload();
	})
	 .fail(function(data) {
		alert(data.responseText);
		return false;
	});
}

function Unix_timestamp(myDate){
			var date = myDate.getFullYear() + "-" + (myDate.getMonth()+1<10 ? "0"+(myDate.getMonth()+1) : (myDate.getMonth()+1)) + "-" 
			+ (myDate.getDate() <10 ?  "0"+myDate.getDate() : myDate.getDate());
			return date;
		}