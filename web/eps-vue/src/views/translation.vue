<template>
	<a-row type='flex'>
		<a-col>
		  <div class='contentBody'>
			<div style='height: 170px;'>
			  <div
				style='width:130px;height:165px; float: left; margin-top: 10px;padding-left:15px;'>
				<div class='zoom'>
				  <img class='zoomOut' title='开启' 
					   src='../assets/video/on.png' @click="getCompetence()">
				  <span style='width: 39px;text-align: center;'>开启</span>
				</div>
				<div class='zoom'>
				  <img class='zoomOut' title='关闭' 
					   src='../assets/video/off.png' @click="stopNavigator()">
				  <span style='width: 39px;text-align: center;'>关闭</span>
				</div>
				<div class='zoom'>
				  <img class='zoomOut' title='运行'
					   src='../assets/video/server.png' @click="server()">
				  <span style='width: 39px;text-align: center;'>运行</span>
				</div>
			  </div>
			</div>
		  </div>
		</a-col>
		<a-col>
		</a-col>
	</a-row>
</template>

<script>
	
</script>

<style lang="less" scoped="scoped">
@import url('translation.less');
</style>
