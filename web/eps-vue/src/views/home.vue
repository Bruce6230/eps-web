<template>
	<div class="home">
		<el-row :gutter="70" type="flex" justify="center">
			<el-col :span="9" class="left">
				<h2>AI综合分析管理云平台</h2>
				<div class="desc">
					<p>协同办公&nbsp;&nbsp;高效办公</p>
					<p>实时监控&nbsp;&nbsp;Ai分析</p>
				</div>
				<div class="bottom">
					<div class="remark-container">
						<div class="ball blue">1</div>
						<div class="remark">实时监控分析</div>
					</div>
					<div class="remark-container">
						<div class="ball red">2</div>
						<div class="remark">在线办公平台</div>
					</div>
					<div class="remark-container">
						<div class="ball green">3</div>
						<div class="remark">云管理平台</div>
					</div>
				</div>
			</el-col>
			<el-col :span="11" class="right">
				<img src="../assets/home/banner.png" class="banner" /></el-col>
		</el-row>
	</div>
</template>

<script></script>

<style lang="less" scoped="scoped">
@import url('home.less');
</style>
