<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
   
    <title>Room {$id}</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link href="../../assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
	<link href="https://getbootstrap.com/examples/cover/cover.css" rel="stylesheet">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<style>
	
	#modalContent 
	{
		border-color: linear-gradient(#333333,#222222); 
		background: #222222;
	}
	#btnModal
	{
		float:right; 
		color:white; 
		margin: 12px 12px 12px 12px; 
		background-color:transparent; 
		border-color:white;
	}
	table tbody::-webkit-scrollbar 
	{
		width:10px;
	}
	table tbody::-webkit-scrollbar-thumb
	{
		border-radius: 6px;
		background: rgba(0,0,0,.1)
	}
	.chatbox
	{
		width:500px;
		height:600px;
		min-height:400px;
		background: rgba(0,0,0,0);
		padding:20px;
		margin:20px auto auto 30px;
	}
	.chatlogs
	{
		padding:10px;
		width: 100%;
		height: 450px;
		background: #eee;
		overflow-y: scroll;
	}
	.chatlogs::-webkit-scrollbar 
	{
		width:10px;
	}
	.chatlogs::-webkit-scrollbar-thumb
	{
		border-radius: 6px;
		background: rgba(0,0,0,.1)
	}
	.chat
	{
		display:flex;
		flex-flow: row wrap;
		align-items: flex-start;
		margin-bottom: 10px;
	}
	.chat .user-name
	{
		color: blue;
		font-weight: bold;
		text-shadow:none;
	}
	
	.chat .chat-message
	{
		width: 70%;	
		padding: 10px;
		margin: 5px 10px 0;
		background-color: blue;
	}
	.chat-form 
	{
		display: flex;
		align-items: flex-start;
		margin-top: 20px;
	}
	#txtMSG
	{
		width:75%;
		height: 50%;
		border: 2px solid #eee;
		border-radius: 3px;
		resize:none;
		padding: 10px;
		font-size:18px;
		color: #999;
	}
	.chat-form button
	{
		background: #1ddced;
		padding: 5px;
		font-size: 25px;
		color: #fff;
		border:none;
		margin: 0 10px;
		border-radius: 3px;
		box-shadow: 0 3px 0 #0eb2c1;
		cursor: pointer;
	}
	</style>
  </head>

  <body onload='setupChat()'>
	
	<div class="container">
	  <div class="modal fade" id="myModal" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body" id="modalContent" >
					  <form method="POST" enctype="multipart/form-data" action="/savefile">
						<table>
							<tr><td><input type="file" name="file" class="btn btn-lg btn-default"/></td>
							<td><input type="submit" value="Upload" class="btn btn-lg btn-default" style="float:right;"/></td></tr>
						</table>
					 </form>
				</div>
			</div>
		</div>
	  </div>
	</div>
	
	<div class="container">
	  <div class="modal fade" id="myModalURL" role="dialog">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-body" id="modalContent" >
					  <p>Save your room url, it's the only way to access it again</p> 
				</div>
			</div>
		</div>
	  </div>
	</div>
	
    <div class="site-wrapper">
    <div class="row">
		<div class="chatbox col-xs-6">
			<div class="chatlogs" id="chatlogs">
				<div class=chat>
				<div class=user-name>System: </div>
				<p class=chat-message>To change username, enter the new name on the textarea and click on change name button</p></div>
				
			
			</div>
		
			<div class="chat-form">
				<textarea id="txtMSG" style="color:black"></textarea>
				<button onclick="SendMessage();">Send</button>
				<button onclick="ChangeName();">Change Name</button>
			</div>
		</div>
			
		<div class="col-xs-6 " style="width:60%;">
		<table style="width:100%; height: 100%;">
			<thead>
			<button id="btnModal" class="btn btn-lg btn-default" data-toggle="modal" data-target="#myModal">Upload</button> 
			</thead>
			<tbody style="display: block; height: 100px; overflow-y: auto;">
				${files}
			</tbody>
		</table>	
		</div>
    </div>
    </div>

    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
   	<script>
		allowChange=true;
		usr="";
		window.onresize = function(event) 
		{
			var size=window.innerHeight-100;
			$('table tbody').css('height', size+'px');
		}
		function setupChat() 
		{
			var size=window.innerHeight-100;
			$('table tbody').css('height', size+'px');
			//$('#myModalURL').modal('show');
		    var updateInterval = 1000;
		     window.setInterval(UpdateChat,updateInterval);
		      user=document.cookie;
			  usr=user.substring(user.indexOf("=")+1,user.length-user.lastIndexOf(";"));
			  if(usr=="")
				  {
				  	usr=Math.floor((Math.random() * 100000) + 1);
				  }
				
		}
		
		function SendMessage()
		{
			var msg="";
			msg=$("#txtMSG").val();
			$("#txtMSG").val("");
			$.ajax({
				type : "POST",
				url : '/savemessage',
				data: {"msg":msg,"user":usr,"date":new Date().toISOString().slice(0, 19).replace('T', ' ')}
				});
		}
		
		function UpdateChat()
		{
			//element = document.getElementById("chatlogs");
			//msgCount =element.getElementsByClassName('chat-message').length.toString();
			var now = new Date();
			now.setSeconds(now.getSeconds() - 0.5);
			$.ajax({
				type : "POST",
				url : '/getmessage',
				data:{"msgCount":now.toISOString().slice(0, 19).replace('T', ' ')},
				dataType: "json",
		        contentType: "application/json;charset=utf-8",
		        success: function(data)
		        {
		        	UpdateChatWindows(data)
		    	}
		    });
		}
		
		function UpdateChatWindows(data)
		{
			var json = $.parseJSON(JSON.stringify(data));
			for(info in json)
				{
					for(data in json[info])
					{
						document.getElementById("chatlogs").innerHTML +=
							"<div class=\"chat\">"+
							"<div class=\"user-name\">"+data+": </div>" +
							"<p class=\"chat-message\">"+json[info][data]+"</p></div>";
					}
					
				}
		}
		
		function ChangeName()
		{
			if(allowChange)
				{
					allowChange=false;
					name=$("#txtMSG").val();
					document.cookie = "username="+name;
					$("#txtMSG").val("");
					usr=name;
					document.getElementById("chatlogs").innerHTML +=
						"<div class=\"chat\">"+
						"<div class=\"user-name\">"+"System: "+"</div>" +
						"<p class=\"chat-message\">"+"Username changed to: "+name+"</p></div>";
				}
			else
				{
				document.getElementById("chatlogs").innerHTML +=
					"<div class=\"chat\">"+
					"<div class=\"user-name\">"+"System: "+"</div>" +
					"<p class=\"chat-message\">"+"Username can't be changed</p></div>";
				}
		}

	</script>
  </body>
</html>
