<?php

$recup=$_POST['texte'];
//$recup="Thank you";
//$recup="hello";



//echo json_encode($response);
//echo json_encode($respnse);

//$recup['reponse']="mercijc";
//echo json_encode($recup);

//$recup="i am a boy";
/*
if(!empty($recup)){
	$response['success']=1;
	$response['message']="ok";
	
}
else{
	$response['success']=0;
	$response['message']="no";
}

echo json_encode($response);
*/
// When you have your own client ID and secret, put them down here:
$CLIENT_ID ="vcemm24@gmail.com";
$CLIENT_SECRET ="f68f8536cb4e4940ba441ea159c47fff";

// Specify your translation requirements here:
$postData = array(
  'fromLang' => "en",
  'toLang' => "fr",
  //'text' => 'i am  fine'
  'text' => $recup
);

$headers = array(
  'Content-Type: application/json',
  'X-WM-CLIENT-ID: '.$CLIENT_ID,
  'X-WM-CLIENT-SECRET: '.$CLIENT_SECRET
);

$url = 'http://api.whatsmate.net/v1/translation/translate';
$ch = curl_init($url);

curl_setopt($ch, CURLOPT_POST, 1);
curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($postData));

$rep = curl_exec($ch);
/*
if(!empty($response)){
	$envoi['reponse']="merci";
}*/
//echo json_encode($envoi);
//echo "Response: ".$response;

if(!empty($recup) and $recup=="hello" ){
	$respnse['success']=1;
	$respnse['message']=$rep;
	//$respnse['message']="hello_retour";
	
}
elseif (!empty($recup) and $recup=="good morning"){
	$respnse['success']=1;
	$respnse['message']=$rep;
}

else {
	$respnse['success']=0;
	$respnse['message']=$rep;
}

echo json_encode($respnse);

curl_close($ch);
?>