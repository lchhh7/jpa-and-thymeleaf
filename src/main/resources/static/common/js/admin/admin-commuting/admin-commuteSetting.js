function adminDetail(id){
          $.ajax({
            url: contextPath + "omissionSchedule/" + id +".do",
            type: "get",
          })
          .done(function(data) {
        	  var form = document.approveForm;
			  form.id.value = data.id;
			  form.memberId.value = data.memberName;
			  form.omissionDt.value = Unix_timestamp(new Date(data.omissionDt));
			  form.attendYn.value = data.attendName;
        	  form.omissionTm.value = data.omissionTm.substr(0,2)+":"+data.omissionTm.substr(2,4);
			  form.content.value = data.content;
        		
        	  var tmp = data.status;
				if(tmp !='R'){
					document.getElementById('adminCommutingApproveButton').style.display = 'none';
					document.getElementById('adminCommutingNoneApproveButton').style.display = 'none';
				}else{
					document.getElementById('adminCommutingApproveButton').style.display = 'inline-block';
					document.getElementById('adminCommutingNoneApproveButton').style.display = 'inline-block';
				}
          })
		}

function approve(status){
	var data ={
		id :  document.approveForm.id.value,
		status : status,
		attendYn : document.approveForm.attendYn.value
	}
	
	$.ajax({
		type: "patch",
        url: contextPath + "omissionApprove.do",
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