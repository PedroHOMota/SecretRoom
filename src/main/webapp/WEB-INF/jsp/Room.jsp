<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../favicon.ico">

    <title>Room {$id}</title>

    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <link href="../../assets/css/ie10-viewport-bug-workaround.css" rel="stylesheet">
	<link href="https://getbootstrap.com/examples/cover/cover.css" rel="stylesheet">
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
	<style>
	.row 
	{
		border-bottom:1px solid white;
	}
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
	
	.chatbox
		{
			width:500px;
			height:600px;
			min-height:400px;
			background: white;
			padding:20px;
			margin:20px auto auto 30px;
			box-shadow: 0 3px #ccc;
			
		}
		.chatlogs
		{
			padding:10px;
			width: 90%;
			height: 450px;
			background: #eee;
			overflow-y: scroll;
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
			width: 50px;	
			height:50px;
			border-radius:50px;
			background-color: white;
		}
		
		.chat .chat-message
		{
			width: 70%;	
			padding: 10px;
			margin: 5px 10px 0;
			background-color: blue;
		}
	</style>
  </head>

  <body>
	
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
	
    <div class="site-wrapper">
    <div class="row">
		<div class="chatbox col-xs-6">
			<div class="chatlogs" id="chatlogs">
				
			</div>
		
			<div class="chat-from">
				<textarea id="txtMSG" style="color:black"></textarea>
				<!--button onclick="SendMessage();">Send</button-->
				<button onclick="UpdateChat();">aaaa</button>
			</div>
		</div>
			
		<div class="col-xs-6 " style="overflow-y: scroll; min-height: 50%; width:63%;">
		<table style="width:100%; ">
			<button content="name" id="btnModal" class="btn btn-lg btn-default" data-toggle="modal" data-target="#myModal">Upload</button> 
			<div >
				${files}

			</div>
		</table>	
		</div>
    </div>
    </div>

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.4/jquery.min.js"></script>
	<script>
		function SendMessage()
		{
			var msg="";
			msg=$("#txtMSG").val();
			
			$.ajax({
				type : "POST",
				url : '/savemessage',
				data: {"msg":msg,"user":"Pedro"}
				
				});
		}
		
		function UpdateChat()
		{
			alert("entrou");
			$.ajax({
				type : "POST",
				url : '/getmessage',
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
					//alert(info); //name
					//alert(json[info]);
					document.getElementById("chatlogs").innerHTML +=
					"<div class=\"chat\">"+
					"<div class=\"user-name\"></div>"+
					"<p class=\"chat-message\">"+json[info]+"</p></div>";
				}
		}
		
		function ChangeName()
		{}
	</script>
  </body>
</html>
