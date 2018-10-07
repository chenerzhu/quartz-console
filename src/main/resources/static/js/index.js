$(function() {

	//run job once
    $(".btnRun").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/runJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            dataType:"JSON",
            success: function(ret) {
                if (ret.valid) {
                	alert("run success!");  
                } else {
                	alert(ret.msg); 
                }
            }
        });
    });
    
    //pause job
    $(".btnPause").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/pauseJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            dataType:"JSON",
            success: function(ret) {
                if (ret.valid) {
                	alert("pause success!");
                	location.reload();
                } else {
                	alert(ret.msg); 
                }
            }
        });
    });
    
    //resume job
    $(".btnResume").click(function() {
    	var jobId = $(this).parent().data("id");
        $.ajax({
            url: "/api/resumeJob?t=" + new Date().getTime(),
            type: "POST",
            data: {
                "jobName": $("#name_"+jobId).text(),
                "jobGroup": $("#group_"+jobId).text()
            },
            dataType:"JSON",
            success: function(ret) {
                if (ret.valid) {
                	alert("resume success!");
                	location.reload();
                } else {
                	alert(ret.msg); 
                }
            }
        });
    });
    
    //delete job
    $(".btnDelete").click(function() {
    	var jobId = $(this).parent().data("id");
    	if(window.confirm("Are you sure?")){
            $.ajax({
                url: "/api/deleteJob?t=" + new Date().getTime(),
                type: "POST",
                dataType:"JSON",
                data: {
                    "jobName": $("#name_"+jobId).text(),
                    "jobGroup": $("#group_"+jobId).text()
                },
                success: function(ret) {
                    if (ret.valid) {
                        alert("delete success!");
                        location.reload();
                    } else {
                        alert(ret.msg);
                    }
                }
            });
    	}
    });
	
	// update
    $(".btnEdit").click(
    		function() {
    			$("#quartzModalLabel").html("cron edit");
    			var jobId = $(this).parent().data("id");
    			$("#jobId").val(jobId);
    			$("#edit_name").val($("#name_"+jobId).text());
    			$("#edit_group").val($("#group_"+jobId).text());
    			$("#cron_val").val($("#cron_"+jobId).text());
    			$("#edit_status").val($("#status_"+jobId).text());
    			$("#edit_desc").val($("#desc_"+jobId).text());
    			$("#edit_type").val($("#type_"+jobId).text());
    			$("#edit_content").val($("#content_"+jobId).text());

    			$('#edit_name').attr("readonly","readonly"); 
    			$('#edit_group').attr("readonly","readonly");
    			$('#edit_type').attr("readonly","readonly");
    			$('#edit_type').attr("disabled","disabled");
    			$('#edit_status').attr("readonly","readonly");
                $('#edit_status').attr("disabled","disabled");
    			
    			$("#quartzModal").modal("show");
    });
    
    $("#save").click(
	    function() {
            $('#mainForm').submit();
	    	/*$.ajax({
	            url: "/api/saveOrUpdate?t=" + new Date().getTime(),
	            type: "POST",
                dataType:"JSON",
	            data:  $('#mainForm').serialize(),
	            success: function(ret) {
	            	if (ret.valid) {
	                	alert("save success!");
	                	location.reload();
	                } else {
	                	alert(ret.msg); 
	                }
	            }
	        });*/
    });


    // create job
    $("#createBtn").click(
    		function() {
    			$("#quartzModalLabel").html("create job");
    			
    			$("#jobId").val("");
    			$("#edit_type").val("java");
                $("#classFile").hide();
                $("#edit_content").attr("placeholder","path or name...");
    			$("#edit_name").val("");
    			$("#edit_group").val("");
    			$("#cronExpression").val("");
    			$("#cron_val").val("");
    			$("#edit_status").val("NORMAL");
    			$("#edit_desc").val("");
    			$("#edit_content").val("");

    			$('#edit_name').removeAttr("readonly");
    			$('#edit_group').removeAttr("readonly");
    			$('#edit_desc').removeAttr("readonly");
                $('#edit_type').removeAttr("readonly");
                $('#edit_type').removeAttr("disabled");
    			
    			$("#quartzModal").modal("show");
    });

    $("#edit_type").change(function () {
        if("dynamic"==$("#edit_type").val()){
            $("#classFile").show();
            $("#edit_content").attr("placeholder","className...");
        }else if("java"==$("#edit_type").val()){
            $("#classFile").hide();
            $("#edit_content").attr("placeholder","path or name...");
        }else if("shell"==$("#edit_type").val()){
            $("#classFile").hide();
            $("#edit_content").attr("placeholder","command...");
        }
    });

    $("#cronExpression").cronGen({
        direction : 'right'
    });
    
});