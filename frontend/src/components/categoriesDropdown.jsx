import StyledLabel from "../styledComponent/styldLabel";
import {FormControl, MenuItem} from "@mui/material";
import StyledSelect from "../styledComponent/styledSelect";

const CategoryDropdown = ({ categories, category, setCategory, required, noneOption, size, sx, sxChild }) => {
    return (
        <FormControl variant="outlined" size={size} sx={sx}>
            <StyledLabel variant="outlined" id="demo-simple-select-label" required={required}>
                Category
            </StyledLabel>
            <StyledSelect
                sx={sxChild}
                labelId="demo-simple-select-label"
                id="demo-simple-select"
                label="Category"
                value={category}
                onChange={(event) => {setCategory(event.target.value)}}
            >
                {categories.map((category) => (
                    <MenuItem
                        key={category.id}
                        value={category.id}
                    >{category.name}</MenuItem>
                ))}
                {noneOption === true && <MenuItem key={0} value={0}>None</MenuItem>}
            </StyledSelect>
        </FormControl>
    );
}

export default CategoryDropdown;