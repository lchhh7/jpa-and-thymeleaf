const documentForm = document.getElementById('document-form');
const joinForm = document.getElementById('join-form');

let index ={
		init : function() {
			$("#btn-save").on("click" , () => {
				if($("#password").val() === $("#checkpw").val()){
					this.save();
				}else {
					alert("비밀번호 확인이 일치하지 않습니다.");
					return ;
				}
			});
			
			
		}, 
		
		save : function(){
			let data ={
					memberId : $("#username").val() , 
					password : $("#password").val() , 
					checkpw : $("#checkpw").val() , 
					name     : $("#name").val() , 
					position : $("#position").val(),
					department : $("#department").val() ,
					phoneNo  : $("#phoneNo").val() ,
					mobileNo  : $("#mobileNo").val() ,
					useColor : $("#color").val()
			}
			
			$.ajax({
				type : "post" , 
				url : contextPath + "/auth/joinProc" , 
				data : JSON.stringify(data),
				contentType : "application/json; charset = utf-8"
			}).done(function(data){
				alert("회원가입이 완료되었습니다");
				location.href = contextPath + "/main.do";
			}).fail(function(data){
				alert(data.responseText);
				return false;
			});
		},
	}
	
	index.init();
	
		/*function save() {
			let data ={
					memberId : $("#username").val() , 
					password : $("#password").val() , 
					name     : $("#name").val() , 
					position : $("#position").val(),
					department : $("#department").val() ,
					phoneNo  : $("#phoneNo").val() ,
					mobileNo  : $("#mobileNo").val() ,
					useColor : $("#color").val()
			};
			
			$.ajax({
				type : "post" , 
				url : contextPath + "auth/joinProc" , 
				data : JSON.stringify(data),
				contentType : "application/json; charset = utf-8"
			}).done(function(data){
				alert("회원가입이 완료되었습니다");
			}).fail(function(error){
				alert(error);
			});
		};
		

document.getElementById('btn-save').addEventListener('click', function(){
	save();
	} , true);
*/
document.addEventListener('DOMContentLoaded', function () {
    //mainVacation();
});

/*function projects(department) {
    $.ajax({
        url: contextPath + "main/project/" + department + ".do",
        method: "get",
    })
        .done(function (data) {
            let tr = '';
            if (data.length > 0) {
                data.forEach(function (el) {
                    tr += '<tr class="tbbody" onclick="location.href=\'' + contextPath + 'project/edit.do?id=' + el.id + '\'">';
                    tr += '<td>' + el.rnum + '</td>';
                    tr += '<td>' + el.title + '</td>';
                    tr += '<td>' + el.orderingName + '</td>';
                    tr += '<td>' + el.startDt + ' ~ ' + el.endDt + "</td>";
                })
            }

            document.getElementById("projects").innerHTML = tr;
        })
        .fail(function () {
            alert("정보 조회 중 오류가 발생했습니다. 관리자에게 문의하세요.");
            return false;
        });
}*/

function commuteInsert(value){
	var data ={
			 attendYn : value
	}
	$.ajax({
		type: "post",
		url: contextPath+"/main/goToWorkButton.do",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
	}).done(function (data, textStatus, xhr) {
			window.location.reload();
	}).fail(function (data, textStatus, xhr) {
		alert("정보 조회 중 오류가 발생했습니다. 관리자에게 문의하세요.");
	});
};

const validPassword = function(_this){
	var elValidMessage = document.querySelector('.valid-message');
	var regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{6,}$/;
	if( !regex.test(_this.value)) {
		elValidMessage.classList.remove('hide');
	}else {
		elValidMessage.classList.add('hide');
	}
};

const checkPassword = function(_this){
	var pw = document.querySelector('#password').value;
	var elMisMatchMessage = document.querySelector('.mismatch-message');
	
	if(pw === _this.value) {
		elMisMatchMessage.classList.add('hide');
	}else {
		elMisMatchMessage.classList.remove('hide');
	}
};