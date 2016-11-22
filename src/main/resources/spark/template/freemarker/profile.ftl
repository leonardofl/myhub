<!DOCTYPE html>
<html>

<body>

	<h1>MyHub profile</h1>

	<h2>My SSH keys registered on GitHub</h2>

	<ul>
	    <#list ssh_keys as ssh_key>
			<li>${ssh_key.title}: ${ssh_key.key}</li>
		</#list>	
	</ul>
</body>
</html>