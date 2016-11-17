import HorizontalChart from './../graphs/horizontalBarChart'

export default class HorizontalDemo {

    constructor()
    {


        this.data1 = [
            {"name": "United States of America", "percentage": 61, color: '#F79221'},
            {"name": "United Kingdom", "percentage": 9, color: '#00AEEF'},
            {"name": "Canada", "percentage": 34, color: '#1EAE5D'},
            {"name": "Brazil", "percentage": 3, color: '#FCBC19'},
            {"name": "India", "percentage": 3, color: '#A9CF38'}, ]

        this.data2 = this.data1.map((r) =>
        {
            let row = JSON.parse(JSON.stringify(r));
            row.percentage = row.percentage + 20;
            return row;
        })

        this.data2 = this.data2.filter((row) => {
            return row.name.indexOf('United') > -1;
        })

        this.data2.push({"name": "Xanadu", "percentage": 95, color: '#ffaadd'})

        this.choices = [this.data1, this.data2]

        let props = {width: 850, height: 200}
        let hel = $('#horizontalMain');

        this.hchart = new HorizontalChart(hel[0], props, "bar1", this.data1);
        this.flip = 0;

    }

    updateData()
    {


        if (this.flip === 0)
        {
            this.flip = 1;
        } else
        {
            this.flip = 0;
        }
        this.hchart.build(this.choices[this.flip])

    }

}